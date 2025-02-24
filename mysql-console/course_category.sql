-- 用sql递归查询树形结构的课程分类表
with recursive t1 as (
    select * from course_category where id = '1'
    union all
    select b.* from course_category b inner join t1 on t1.id = b.parentid
)
select * from t1 order by t1.orderby, t1.id