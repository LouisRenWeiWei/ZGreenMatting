package com.zgreenmatting.blservice;

import com.igoda.dao.utils.DaoUtils;


public class MattingImageService {
    private static MattingImageService instance = new MattingImageService();

    public static MattingImageService getInstance() {
        return instance;
    }

    private MattingImageService() {
    }

    public void selectList() {
        DaoUtils.getDaoSession().getMattingImageDao().queryBuilder()
                .list();
    }

}
