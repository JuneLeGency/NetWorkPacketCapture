package com.minhui.networkcapture;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.minhui.vpn.ThreadProxy;
import com.minhui.vpn.VPNConnectManager;
import com.minhui.vpn.VPNConstants;

import java.io.File;
import java.io.FileFilter;

/**
 * @author minhui.zhu
 *         Created by minhui.zhu on 2018/5/5.
 *         Copyright © 2017年 Oceanwing. All rights reserved.
 */

public class SettingFragment extends BaseFragment {

    private Handler handler;

    @Override
    int getLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.clear_cache_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHistoryData();
            }
        });
        view.findViewById(R.id.about_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });
        handler = new Handler();
    }

    private void clearHistoryData() {
        ThreadProxy.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                File file = new File(VPNConstants.BASE_DIR);
                FileUtils.deleteFile(file, new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if (!pathname.exists()) {
                            return false;
                        }

                        String lastVpnStartTimeStr = VPNConnectManager.getInstance().getLastVpnStartTimeStr();
                        if (lastVpnStartTimeStr == null) {
                            return true;
                        }
                        String absolutePath = pathname.getAbsolutePath();
                        //如果所选择文件是最近一次产生的，则不删除
                        return !absolutePath.contains(lastVpnStartTimeStr);
                    }
                });
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showMessage(getString(R.string.success_clear_history_data));
                    }
                });

            }
        });


    }

    private void showMessage(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }
}
