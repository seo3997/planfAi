package com.whomade.planfAi.admin.author.mapper;

import com.whomade.planfAi.admin.author.vo.AuthorVo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AuthorMapper {
    int selectTotCntAuthor(AuthorVo authorVo);

    List<AuthorVo> selectPageListAuthor(AuthorVo authorVo);

    AuthorVo selectAuthor(String authorId);

    int insertAuthor(AuthorVo authorVo);

    int updateAuthor(AuthorVo authorVo);

    int deleteAuthor(String authorId);

    String selectExistYnAuthor(String authorId);
}
