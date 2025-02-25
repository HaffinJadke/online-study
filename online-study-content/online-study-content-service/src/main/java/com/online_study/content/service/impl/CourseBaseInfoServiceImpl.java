package com.online_study.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online_study.base.exception.OnlineStudyException;
import com.online_study.base.model.PageParams;
import com.online_study.base.model.PageResult;
import com.online_study.content.mapper.CourseBaseMapper;
import com.online_study.content.mapper.CourseCategoryMapper;
import com.online_study.content.mapper.CourseMarketMapper;
import com.online_study.content.service.CourseBaseInfoService;
import com.online_study.model.dto.AddCourseDto;
import com.online_study.model.dto.CourseBaseInfoDto;
import com.online_study.model.dto.EditCourseDto;
import com.online_study.model.dto.QueryCourseParamsDto;
import com.online_study.model.po.CourseBase;
import com.online_study.model.po.CourseCategory;
import com.online_study.model.po.CourseMarket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto) {

        //mybatis-plus的wrapper条件（根据课程名、审核状态和发布状态查询）
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName()),
                        CourseBase::getName, courseParamsDto.getCourseName())
                .eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus()),
                        CourseBase::getAuditStatus,courseParamsDto.getAuditStatus())
                .eq(StringUtils.isNotEmpty(courseParamsDto.getPublishStatus()),
                        CourseBase::getStatus,courseParamsDto.getPublishStatus());

        //分页参数模型类转为mybatis-plus的分页参数
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(),pageParams.getPageSize());

        //查询结果
        Page<CourseBase> result = courseBaseMapper.selectPage(page, wrapper);
        List<CourseBase> items = result.getRecords();
        long total = result.getTotal();

        //返回结果
        PageResult<CourseBase> pageResult = new PageResult<CourseBase>(items, total,
                pageParams.getPageNo(), pageParams.getPageSize());

        return pageResult;
    }

    /**
     * 向CourseBase表和CourseMarket两张表插入数据
     * @param companyId 机构id（用户单点登录获取）
     * @param addCourseDto （课程信息）
     * @return
     */
    @Override
    @Transactional(rollbackFor = {OnlineStudyException.class, Exception.class}) //指定声明式事务对自定义的全局异常类生效
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto) {

        //// 一般对model层的模型类属性加上注解校验，在controller层的方法参数上开启注解校验
        //// 只有不走controller层直接调用service层的特殊情况才对service进行参数校验，因为service层的@Validated注解不会生效

        //向CourseBase表插入数据
        CourseBase courseBaseNew = new CourseBase();

        //把前者相同的字段复制给后者，注意null也会复制
        BeanUtils.copyProperties(addCourseDto, courseBaseNew);

        //未传入的数据单独set
        courseBaseNew.setCompanyId(companyId);
        //TODO：机构名称从数据字典查
        courseBaseNew.setCreateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        //修改时间这里不考虑
        //TODO:创建人和修改人后期引入权限后添加
        //审核状态默认未提交
        courseBaseNew.setAuditStatus("202002");
        //发布状态默认未发布
        courseBaseNew.setStatus("203001");

        int insert = courseBaseMapper.insert(courseBaseNew);
        if(insert<=0) {
            OnlineStudyException.cast("添加课程失败");
        }

        //向CourseMarket表插入数据
        CourseMarket courseMarketNew = new CourseMarket();
        BeanUtils.copyProperties(addCourseDto, courseMarketNew);
        courseMarketNew.setId(courseBaseNew.getId()); //设置主键

        int insert2 = saveCourseMarket(courseMarketNew);
        if(insert2<=0) {
            OnlineStudyException.cast("添加课程失败");
        }

        //查询需要返回的信息
        CourseBaseInfoDto courseCreateInfo = getCourseBaseInfo(courseBaseNew.getId());
        return courseCreateInfo;
    }

    /**
     * 保存CourseMarket信息的方法；字段存在则更新，不存在则插入
     */
    private int saveCourseMarket(CourseMarket courseMarketNew) {

        //TODO:关于原价现价、是否免费的校验逻辑
        //参数合法性校验
        String charge = courseMarketNew.getCharge();
        if(StringUtils.isEmpty(charge)) {
            OnlineStudyException.cast("收费规则为空");
        }

        if("201001".equals(charge)) {
            if(courseMarketNew.getPrice() == null || courseMarketNew.getPrice()<=0
                || courseMarketNew.getOriginalPrice() == null || courseMarketNew.getOriginalPrice()<=0) {
                OnlineStudyException.cast("课程为收费课程，价格不能为空且必须大于0");
            }
        }

        int rows = 0;
        Long id = courseMarketNew.getId();
        CourseMarket courseMarket = courseMarketMapper.selectById(id);
        if(courseMarket == null) {
            rows = courseMarketMapper.insert(courseMarketNew);
        } else {
            rows = courseMarketMapper.updateById(courseMarketNew);
        }

        return rows;
    }

    /**
     * 新增课程后、修改课程前，需要按照课程id查询信息
     */
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId) {

        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null) {
            return null;
        }

        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);

        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
        if(courseMarket != null) {
            BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);
        }

        //设置stName和mtName
        //TODO:stName和mtName用缓存或者sql语句优化
        CourseCategory courseCategory = courseCategoryMapper.selectById(courseBaseInfoDto.getMt());
        courseBaseInfoDto.setMtName(courseCategory.getName());
        courseCategory = courseCategoryMapper.selectById(courseBaseInfoDto.getSt());
        courseBaseInfoDto.setStName(courseCategory.getName());

        return courseBaseInfoDto;
    }

    /**
     * 修改课程实现，会调用getCourseBaseInfo方法
     * @param companyId 机构id（用户单点登录获取）
     * @param editCourseDto （修改课程信息）
     * @return
     */
    @Override
    @Transactional(rollbackFor = {OnlineStudyException.class, Exception.class})
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {

        Long courseId = editCourseDto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null) {
            OnlineStudyException.cast("课程不存在");
        }
        //TODO：机构id校验后续进行
//        if(courseBase.getCompanyId().longValue() != companyId) {
//            OnlineStudyException.cast("无权限修改其他机构的课程");
//        }

        BeanUtils.copyProperties(editCourseDto, courseBase);
        courseBase.setChangeDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        //TODO:修改人，分类字段

        int row = courseBaseMapper.updateById(courseBase);
        if(row<=0) {
            OnlineStudyException.cast("修改课程基本信息失败");
        }

        //修改营销信息
        //TODO:调用saveCourseMarket
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        int row2 = 0;
        if(courseMarket == null) {
            CourseMarket courseMarketNew = new CourseMarket();
            BeanUtils.copyProperties(editCourseDto, courseMarketNew);
            row2 = courseMarketMapper.insert(courseMarketNew);
        } else {
            row2 = courseMarketMapper.updateById(courseMarket);
        }
        if(row2<=0) {
            OnlineStudyException.cast("修改课程营销信息失败");
        }

        CourseBaseInfoDto courseUpdateInfo = getCourseBaseInfo(courseId);
        return courseUpdateInfo;
    }
}
