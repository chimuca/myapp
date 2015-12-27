package com.messake.messake.ercode;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.messake.messake.view.ErcodeScanView;

public final class ViewfinderResultPointCallback implements ResultPointCallback {

    private final ErcodeScanView viewfinderView;

    public ViewfinderResultPointCallback(ErcodeScanView viewfinderView) {
        this.viewfinderView = viewfinderView;
    }

    public void foundPossibleResultPoint(ResultPoint point) {
        viewfinderView.addPossibleResultPoint(point);
    }

}

