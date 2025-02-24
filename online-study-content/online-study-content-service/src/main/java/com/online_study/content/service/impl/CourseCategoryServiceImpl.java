package com.online_study.content.service.impl;

import com.online_study.content.mapper.CourseCategoryMapper;
import com.online_study.content.service.CourseCategoryService;
import com.online_study.model.dto.CourseCategoryTreeDto;
import com.online_study.model.po.CourseCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    /**
     * 从传入的根节点中获取所有子节点
     * @param id 要查询的根节点
     * @return 返回根节点的下一层节点的list
     */
    @Override
    public List<CourseCategoryTreeDto> queryCategoryTree(String id) {
        //这里查询到的是包含根节点（id）的整个数据库表，需要处理为树结构
        List<CourseCategoryTreeDto> courseCategoryTree = courseCategoryMapper.selectCategoryTree(id);

        //将list转为map
        Map<String, CourseCategoryTreeDto> mapTemp = courseCategoryTree.stream()
                //filter是过滤器，过滤掉根节点；当filter内的判断为假时就排除
                .filter(item->!id.equals(item.getId())).collect(Collectors.toMap(
                //第一个参数是map的key，第二个参数是map的value，第三个参数表示如果key重复，则用第二个参数覆盖第一个参数
                CourseCategory::getId, value -> value, (key1, key2) -> key2));

        //定义一个list存结果
        List<CourseCategoryTreeDto> courseCategoryChildrenTree = new ArrayList<>();

        //同理，先过滤掉根节点，然后遍历所有数据项
        courseCategoryTree.stream().filter(item->!id.equals(item.getId())).forEach(item -> {
            // 如果是传入的根（id）的下一级子节点，则添加到返回结果中
            if(item.getParentid().equals((id))) {
                courseCategoryChildrenTree.add(item);
            }

            //从mapTemp中获取该节点的父节点（mapTemp已经将整棵树的根节点排除了）
            CourseCategoryTreeDto father = mapTemp.get(item.getParentid());
            //父节点不为空的前提下，如果children属性为空，则创建一个children属性；否则把stream流的当前item添加到children属性中
            if(father!=null) {
                if(father.getChildrenTreeNodes()==null) {
                    father.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                }
                father.getChildrenTreeNodes().add(item);
            }
        });

        return courseCategoryChildrenTree;
    }
}
