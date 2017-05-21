package com.zgreenmatting.blservice;

import android.text.TextUtils;

import com.igoda.dao.MattingImageDao;
import com.igoda.dao.TempImageDao;
import com.igoda.dao.entity.MattingImage;
import com.igoda.dao.entity.TempImage;
import com.igoda.dao.utils.DaoUtils;
import com.zgreenmatting.entity.DownloadStatus;
import com.zgreenmatting.entity.ProgressInfo;
import com.zgreenmatting.utils.FileUtils;

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
            //url相同，但是value不一样
            if(!mattingImage.getValue().equals(tmp.getValue())){
                if(!TextUtils.isEmpty(tmp.getSdPath())){
                    FileUtils.deleteFile(tmp.getSdPath());
                }
                dao.delete(tmp);
                dao.save(mattingImage);
            }
            /*
            else{//url 和value都一样不需要做什么操作
                tmp.setName(mattingImage.getName());
                tmp.setCreateTime(mattingImage.getCreateTime());
                tmp.setExt(mattingImage.getExt());
                dao.update(tmp);
            }
            */
        }else {
            dao.save(mattingImage);
        }
    }
    //删除除这些之外的
    public void deleteWithout(List<String> exist) {
        MattingImageDao dao = DaoUtils.getDaoSession().getMattingImageDao();
        List<MattingImage> needDelete = dao.queryBuilder().where(MattingImageDao.Properties.Url.notIn(exist)).list();
        for (MattingImage item :needDelete) {
            if(!TextUtils.isEmpty(item.getSdPath())){
                FileUtils.deleteFile(item.getSdPath());
            }
            dao.delete(item);
        }
    }


    //修改
    public void update(MattingImage mattingImage){
        MattingImageDao dao = DaoUtils.getDaoSession().getMattingImageDao();
        dao.update(mattingImage);
    }

    //获取下载进度信息
    public ProgressInfo getProgressInfo() {
        ProgressInfo info = new ProgressInfo();
        MattingImageDao dao = DaoUtils.getDaoSession().getMattingImageDao();
        info.setTotal(dao.queryBuilder().count());
        info.setFinished(dao.queryBuilder().where(
                MattingImageDao.Properties.DownloadState.eq(DownloadStatus.DONE.getValue())
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
