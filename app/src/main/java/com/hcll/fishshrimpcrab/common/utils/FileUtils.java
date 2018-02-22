package com.hcll.fishshrimpcrab.common.utils;

import android.os.Environment;
import android.text.format.DateUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * Created by lWX524664 on 2017/11/24.
 */

public class FileUtils {
    /**
     * sd卡的根目录
     */
    public static final String FILE_ROOT = Environment.getExternalStorageDirectory() + "/wu2dou/";

    /**
     * 图片缓存路径
     */
    public static final String IMAGE_CACHE_ROOT = FILE_ROOT + "image";
    public static final String VIDEO_CACHE_ROOT = FILE_ROOT + "video";
    public static final String AUDIO_CACHE_ROOT = FILE_ROOT + "audio";


    /**
     * 创建图片存储路径
     *
     * @return
     */
    public static String createCaptureFilePath() {
        String imagePath = FileUtils.IMAGE_CACHE_ROOT + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg";
        //校验文件夹是否存在
        File file = new File(imagePath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return imagePath;
    }

    /**
     * 创建视频存储路径
     *
     * @return
     */
    public static String createVideoFilePath() {
        String videoPath = FileUtils.VIDEO_CACHE_ROOT + File.separator + "VID_" + System.currentTimeMillis() + ".mp4";
        //校验文件夹是否存在
        File file = new File(videoPath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return videoPath;
    }

    /**
     * 创建录音存储路径
     *
     * @return
     */
    public static String createAudioFilePath() {
        String videoPath = FileUtils.AUDIO_CACHE_ROOT + File.separator + "AUD_" + System.currentTimeMillis() + ".mp3";
        //校验文件夹是否存在
        File file = new File(videoPath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return videoPath;
    }

    /***
     * 清空指定文件夹/文件
     *
     * @return 清空成功的话返回true, 否则返回false
     */

    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] childs = file.listFiles();
            if (childs == null || childs.length <= 0) {
                // 空文件夹删掉
                return file.delete();
            } else {
                // 非空，遍历删除子文件
                for (int i = 0; i < childs.length; i++) {
                    deleteFile(childs[i]);
                }
                return deleteFile(file);
            }
        } else {
            return file.delete();
        }

    }

    public static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
