package cf.connota.sunrinton.Activity;

import android.databinding.DataBindingUtil;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cf.connota.sunrinton.R;
import cf.connota.sunrinton.Util.TOOHardWorkManger;
import cf.connota.sunrinton.databinding.ActivityResponsePlzBinding;

public class ResponsePlz extends AppCompatActivity {
    ActivityResponsePlzBinding binding;
    TOOHardWorkManger worker;
    Camera c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_response_plz);

        worker = new TOOHardWorkManger(this);
        c = Camera.open();

        binding.v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true)
                            blankFlash();
                    }
                }).start();

            }
        });

        binding.v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (worker.run) {
                            try {
                                SystemClock.sleep(1000);
                                worker.ringAlr();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });

        binding.v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                worker.run = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true)
                            worker.ringVib();
                    }
                }).start();
            }
        });

        binding.v4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                worker.stopAlr();
            }
        });

        binding.v5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                worker.talk(binding.edt.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });


        binding.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                worker.stopAlr();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        worker.destroyTTS();
        if (c != null)
            c.release();
    }

    private void blankFlash() {
        if (c.getParameters().getFlashMode().equals("torch")) {
            c.release();
            c = Camera.open();
        } else {
            Camera.Parameters p = c.getParameters();
            p.setFlashMode("torch");
            c.setParameters(p);
        }
    }

}
