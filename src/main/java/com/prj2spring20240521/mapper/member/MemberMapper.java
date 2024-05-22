package com.prj2spring20240521.mapper.member;

import com.prj2spring20240521.domain.member.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {
    @Insert("""
                INSERT INTO member(email, password, nick_name)
                VALUES (#{email}, #{password}, #{nickName})
            """)
    int insert(Member member);

    @Select("""
                SELECT *
                FROM member
                WHERE email = #{email}
            """)
    Member selectByEmail(String email);
}
