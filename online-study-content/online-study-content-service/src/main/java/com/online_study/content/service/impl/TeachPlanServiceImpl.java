package com.online_study.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.online_study.base.exception.ErrorEnum;
import com.online_study.base.exception.OnlineStudyException;
import com.online_study.content.mapper.TeachplanMapper;
import com.online_study.content.mapper.TeachplanMediaMapper;
import com.online_study.content.service.TeachPlanService;
import com.online_study.model.dto.SaveTeachplanDto;
import com.online_study.model.dto.TeachPlanDto;
import com.online_study.model.po.Teachplan;
import com.online_study.model.po.TeachplanMedia;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeachPlanServiceImpl implements TeachPlanService {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Override
    public List<TeachPlanDto> findTeachPlanTree(Long courseId) {
        List<TeachPlanDto> teachPlanDtos = teachplanMapper.selectTreeNodes(courseId);
        return teachPlanDtos;
    }

    /**
     * 获取当前同级节点的最大排序字段
     * @param parentId
     * @param courseId
     * @return
     */
    private Integer getOrderby(Long parentId, Long courseId) {
        //查找同级节点的数量
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(Teachplan::getCourseId, courseId).eq(Teachplan::getParentid, parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        //确定当前字段的排序等级
        Integer maxOrderby = 0;
        if(count != 0) { //当同级节点数量不为0时，查询同级节点的最大排序字段
            maxOrderby = teachplanMapper.selectMaxOrderby(parentId, courseId);
        }
        return maxOrderby+1;
    }

    @Override
    @Transactional(rollbackFor = {OnlineStudyException.class, Exception.class})
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        //通过前端是否传入教学计划id判断是修改还是新增
        Long teachplanId = saveTeachplanDto.getId();
        //新增（前端传入数据的时候就已经定义了当前是大章节还是小章节）
        if(teachplanId == null) {
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(saveTeachplanDto, teachplan);

            Long parentId = saveTeachplanDto.getParentid();
            Long courseId = saveTeachplanDto.getCourseId();

            teachplan.setOrderby(getOrderby(parentId, courseId));
            teachplanMapper.insert(teachplan);
        } else {
            Teachplan teachplan = teachplanMapper.selectById(teachplanId);
            BeanUtils.copyProperties(saveTeachplanDto, teachplan);
            teachplanMapper.updateById(teachplan);
        }
    }

    /**
     * 获取大章节下的小章节数量
     * @param parentId
     * @return
     */
    private int getSonCount(Long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(Teachplan::getParentid, parentId);
        return teachplanMapper.selectCount(queryWrapper);
    }

    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;

    /**
     * 删除课程计划
     * @param id
     */
    @Override
    @Transactional(rollbackFor = {OnlineStudyException.class, Exception.class})
    public void removeTeachplan(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        //当前节点是否存在
        if(teachplan == null) {
            OnlineStudyException.cast("章节不存在");
        }
        //当前节点是大章节
        if(teachplan.getParentid() == 0) {
            int count = getSonCount(id);
            //判断有没有小章节
            if(count > 0) {
                OnlineStudyException.cast("当前大章节下有子章节，不能删除");
            }
            teachplanMapper.deleteById(id);
            return;
        }

        //当前节点是小章节
        teachplanMapper.deleteById(id);
        //小章节还需要删除关联的media表
        LambdaQueryWrapper<TeachplanMedia> mediaWrapper = new LambdaQueryWrapper<>();
        mediaWrapper = mediaWrapper.eq(TeachplanMedia::getTeachplanId, id);
        teachplanMediaMapper.delete(mediaWrapper);
    }

    /**
     * 获取当前节点同级节点的集合和当前节点在集合中的索引
     * @param teachplan
     * @return
     */
    private Map<String,Object> getListAndIndex(Teachplan teachplan) {
        //同级节点按照orderby字段升序查询
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper
                .eq(Teachplan::getCourseId, teachplan.getCourseId())
                .eq(Teachplan::getParentid, teachplan.getParentid())
                .orderByAsc(Teachplan::getOrderby);
        List<Teachplan> teachplanList = teachplanMapper.selectList(queryWrapper);

        for(int index = 0; index < teachplanList.size(); index++) {
            if(teachplanList.get(index).getId().equals(teachplan.getId())) {
                Map<String, Object> map = new HashMap<>();
                map.put("teachplanList", teachplanList);
                map.put("index", index);
                return map;
            }
        }

        return null;
    }

    /**
     * 更换当前节点与另一个上移或下移节点的排序字段
     * @param teachplan
     * @param teachplanOther
     */
    private void exchangeOrderby(Teachplan teachplan, Teachplan teachplanOther) {
        Integer orderby = teachplan.getOrderby();
        teachplan.setOrderby(teachplanOther.getOrderby());
        teachplanOther.setOrderby(orderby);
        teachplanMapper.updateById(teachplan);
        teachplanMapper.updateById(teachplanOther);
    }

    /**
     * 课程计划上移或下移
     * @param id
     * @param upOrDown
     */
    @Override
    @Transactional(rollbackFor = {OnlineStudyException.class, Exception.class})
    public void moveUpOrDownTeachplan(Long id, boolean upOrDown) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        //检查节点是否存在
        if(teachplan == null) {
            OnlineStudyException.cast("章节不存在");
        }
        //获取当前节点同级节点的集合
        Map<String, Object> map = getListAndIndex(teachplan);
        List<Teachplan> teachplanList = (List<Teachplan>) map.get("teachplanList");
        int index = (int) map.get("index");

        if(upOrDown) { //上移
            int minOrderby = teachplanMapper.selectMinOrderby(teachplan.getParentid(), teachplan.getCourseId());
            if(teachplan.getOrderby()==minOrderby) {
                OnlineStudyException.cast("当前节点不能上移");
            }
            Teachplan teachplanPrev = teachplanList.get(index-1);
            exchangeOrderby(teachplan, teachplanPrev);
        } else { //下移
            int maxOrderby = teachplanMapper.selectMaxOrderby(teachplan.getParentid(), teachplan.getCourseId());
            if(teachplan.getOrderby()==maxOrderby) {
                OnlineStudyException.cast("当前节点不能下移");
            }
            Teachplan teachplanNext = teachplanList.get(index+1);
            exchangeOrderby(teachplan, teachplanNext);
        }
    }

}
