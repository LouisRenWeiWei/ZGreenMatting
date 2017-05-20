package com.zgreenmatting.blservice;

import com.igoda.dao.MattingImageDao;
import com.igoda.dao.TempImageDao;
import com.igoda.dao.entity.MattingImage;
import com.igoda.dao.entity.TempImage;
import com.igoda.dao.utils.DaoUtils;
import com.zgreenmatting.download.status.DownloadStatus;
import com.zgreenmatting.entity.ProgressInfo;

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

    public ProgressInfo getProgressInfo() {
        ProgressInfo info = new ProgressInfo();
        MattingImageDao dao = DaoUtils.getDaoSession().getMattingImageDao();
        info.setTotal(dao.queryBuilder().count());
        info.setFinished(dao.queryBuilder().where(
                MattingImageDao.Properties.DownloadState.eq(DownloadStatus.DONE)
        ).count());
        return info;
    }

    public int getLocalUnuploadCount() {
        return DaoUtils.getDaoSession().getTempImageDao().queryBuilder().list().size();
    }

    public void saveTmpImage(String picPath, String hash) {
        TempImageDao dao = DaoUtils.getDaoSession().getTempImageDao();
        TempImage tmp = dao.queryBuilder().where(TempImageDao.Properties.Value.eq(hash)).unique();
        if(tmp==null){
            tmp = new TempImage();
            tmp.setSdPath(picPath);
            tmp.setValue(hash);
            dao.save(tmp);
        }else {
            tmp.setSdPath(picPath);
            dao.update(tmp);
        }
    }

    public void deleteTmpImage(TempImage tempImage) {
        DaoUtils.getDaoSession().getTempImageDao().delete(tempImage);
    }

    public TempImage getNextTmpImage() {
        return DaoUtils.getDaoSession().getTempImageDao().queryBuilder().offset(0).limit(1).unique();
    }


}
