package com.dullyoung.jetpackdemo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Dullyoung
 * Created by　Dullyoung on 2021/4/6
 **/
public class MWorkManager extends Worker {
    public MWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String path = getApplicationContext().getCacheDir() + "/haha";
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        File file = new File(path, "hahah.txt");
        try {
            if (file.exists() || file.createNewFile()) {
                FileWriter fileWriter = new FileWriter(file, true);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd aHH:mm:ss", Locale.CHINA);
                fileWriter.append("\n").append(format.format(new Date()));
                fileWriter.flush();
                fileWriter.close();
                Log.i("aaaa", "onCreate:write ");
                return Result.success();
            } else {
                Log.i("aaaa", "doWork: 文件不存在且创建失败");
                return Result.failure();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("aaaa", "doWork: " + e);
            return Result.failure();
        }
    }
}
