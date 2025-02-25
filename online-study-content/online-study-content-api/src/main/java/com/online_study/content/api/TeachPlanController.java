package com.online_study.content.api;

import com.online_study.content.service.TeachPlanService;
import com.online_study.model.dto.SaveTeachplanDto;
import com.online_study.model.dto.TeachPlanDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value="课程计划查询接口", tags="课程计划查询接口")
public class TeachPlanController {

    @Autowired
    TeachPlanService teachPlanService;

    @ApiOperation("查询课程计划树形结构")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachPlanDto> getTreeNodes(@PathVariable Long courseId) {
        List<TeachPlanDto> teachPlanTree = teachPlanService.findTeachPlanTree(courseId);
        return teachPlanTree;
    }

    @ApiOperation("新增大章节、小章节，修改章节信息的接口")
    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody SaveTeachplanDto saveTeachplanDto) {
        teachPlanService.saveTeachplan(saveTeachplanDto);
    }
}
