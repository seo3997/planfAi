package com.whomade.planfAi.admin.author.service;

import com.whomade.planfAi.admin.author.vo.AuthorVo;
import java.util.List;

public interface AuthorService {
    int getTotCntAuthor(AuthorVo authorVo);

    List<AuthorVo> getPageListAuthor(AuthorVo authorVo);

    AuthorVo getAuthorDetail(String authorId);

    void saveAuthor(AuthorVo authorVo);

    void updateAuthor(AuthorVo authorVo);

    void deleteAuthor(String authorId);

    boolean isExistAuthor(String authorId);
}
