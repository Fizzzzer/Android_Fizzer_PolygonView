package com.fizzer.doraemon.polygonview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    private PolygonView mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = (PolygonView) findViewById(R.id.view);
    }

    public void Reflash(View view){
        mView.invalidate();
    }
}
