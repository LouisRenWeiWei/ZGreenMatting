package com.zgreenmatting.blservice;

import com.igoda.dao.MattingImageDao;
import com.igoda.dao.entity.MattingImage;
import com.igoda.dao.utils.DaoUtils;
import com.zgreenmatting.download.status.DownloadStatus;

import java.util.List;


public class MattingImageService {
    private static MattingImageService instance = new MattingImageService();

    public static MattingImageService getInstance() {
        return instance;
    }

    private MattingImageService() {
    }

    public List<MattingImage> getList() {
        return DaoUtils.getDaoSession().getMattingImageDao().queryBuilder()
                .list();
    }
    //新增数据
    public void save(MattingImage mattingImage){
        MattingImageDao dao = DaoUtils.getDaoSession().getMattingImageDao();
        MattingImage tmp = dao.queryBuilder().where(MattingImageDao.Properties.Url.eq(mattingImage.getUrl())).unique();
        if(tmp!=null){
            if(!mattingImage.getValue().equals(tmp.getValue())){
                mattingImage.setId(tmp.getId());
                dao.update(mattingImage);
            }else{
                dao.update(tmp);
            }
        }else {
            dao.save(mattingImage);
        }
    }
    //修改
    public void update(MattingImage mattingImage){
        MattingImageDao dao = DaoUtils.getDaoSession().getMattingImageDao();
        dao.update(mattingImage);
    }

}
