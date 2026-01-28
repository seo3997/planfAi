package com.whomade.planfAi.admin.author.service;

import com.whomade.planfAi.admin.author.mapper.AuthorMapper;
import com.whomade.planfAi.admin.author.vo.AuthorVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorMapper authorMapper;

    @Override
    public int getTotCntAuthor(AuthorVo authorVo) {
        return authorMapper.selectTotCntAuthor(authorVo);
    }

    @Override
    public List<AuthorVo> getPageListAuthor(AuthorVo authorVo) {
        return authorMapper.selectPageListAuthor(authorVo);
    }

    @Override
    public AuthorVo getAuthorDetail(String authorId) {
        return authorMapper.selectAuthor(authorId);
    }

    @Override
    public void saveAuthor(AuthorVo authorVo) {
        if ("Y".equals(authorMapper.selectExistYnAuthor(authorVo.getAuthorId()))) {
            throw new IllegalArgumentException("이미 존재하는 권한 ID입니다.");
        }
        authorMapper.insertAuthor(authorVo);
    }

    @Override
    public void updateAuthor(AuthorVo authorVo) {
        authorMapper.updateAuthor(authorVo);
    }

    @Override
    public void deleteAuthor(String authorId) {
        authorMapper.deleteAuthor(authorId);
    }

    @Override
    public boolean isExistAuthor(String authorId) {
        return "Y".equals(authorMapper.selectExistYnAuthor(authorId));
    }
}
