package demo.alexey.com.rxdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    private TextView tvSimpleMain;
    private TextView tvComplicatMain;
    private String[] mDatas = {"data1", "data2", "data3", "data4", "data5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvSimpleMain = (TextView) findViewById(R.id.tv_simple_main);
        tvComplicatMain = (TextView) findViewById(R.id.tv_complicat_main);
        //最简单的rxjava测试
        simpleTest();
        //精简过后的rxjava
        complicatTest();

    }
    //点击按钮后,对操作符测试
    public  void onOpraterClicked(View v) {
        Observable.just(mDatas)
                //输入数组并且一条条拿给subscribe
                .flatMap(new Func1<String[], Observable<String>>() {
                    @Override
                    public Observable<String> call(String[] strings) {
                        return Observable.from(strings);
                    }
                })
                //过滤字符串为data1的数据
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !("data1".equals(s));
                    }
                })
                //限定输出数据的数量
                .take(3)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("tag",s+"\n");
                    }
                });
    }

    //精简后的rxjava
    private void complicatTest() {
        Observable
                .just("this from complicat rxjava")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s + "  was changed";
                    }
                })
                .take(3)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        tvComplicatMain.setText(s);
                    }
                });
    }

    //最简单的rxjava测试
    private void simpleTest() {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                tvSimpleMain.setText(s);
            }
        };
        Observable observer = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("this world form simple observers");
            }
        });
        observer.subscribe(subscriber);
    }
}
