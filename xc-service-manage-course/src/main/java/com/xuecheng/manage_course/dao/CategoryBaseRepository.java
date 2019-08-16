package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator.
 */
public interface CategoryBaseRepository extends JpaRepository<Category,String> {
}
