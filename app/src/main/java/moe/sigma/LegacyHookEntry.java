package moe.sigma;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Entry point for started Xposed API 51-99.
 * <p>
 * Xposed is used as ART hook implementation.
 */

public class LegacyHookEntry implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    private final ArrayList<String> tried_surname_list = new ArrayList<>();
    public boolean stop_flag = false;
    public boolean start_flag = false;
    List<String> lst_surname = Arrays.asList(
            "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨",
            "朱", "秦", "尤", "许", "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜",
            "戚", "谢", "邹", "喻", "柏", "水", "刘", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
            "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐",
            "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常",
            "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄",
            "和", "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧",
            "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒", "屈", "项", "祝", "董", "梁",
            "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危", "江", "童", "颜", "郭",
            "梅", "盛", "林", "刁", "钟", "徐", "邱", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍",
            "虞", "万", "支", "柯", "昝", "管", "卢", "莫", "经", "房", "裘", "缪", "干", "解", "应", "宗",
            "丁", "宣", "贲", "邓", "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉", "钮", "龚",
            "程", "嵇", "邢", "滑", "裴", "陆", "荣", "翁", "荀", "羊", "於", "惠", "甄", "曲", "家", "封",
            "芮", "羿", "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫", "乌", "焦", "巴", "弓",
            "牧", "隗", "山", "谷", "车", "侯", "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫",
            "宁", "仇", "栾", "暴", "甘", "钭", "厉", "戎", "祖", "武", "符", "景", "詹", "束", "龙", "窦",
            "叶", "幸", "司", "韶", "郜", "黎", "蓟", "薄", "印", "宿", "白", "怀", "蒲", "邰", "从", "鄂",
            "索", "咸", "籍", "赖", "卓", "蔺", "屠", "蒙", "池", "乔", "阴", "郁", "胥", "能", "苍", "双",
            "闻", "莘", "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵", "冉", "宰", "郦", "雍",
            "郤", "璩", "桑", "桂", "濮", "牛", "寿", "通", "边", "扈", "燕", "冀", "郏", "浦", "尚", "农",
            "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连", "茹", "习", "宦", "艾", "鱼", "容",
            "向", "古", "易", "慎", "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿", "满", "弘",
            "匡", "国", "文", "寇", "广", "禄", "阙", "东", "欧", "殳", "沃", "利", "蔚", "越", "夔", "隆",
            "师", "巩", "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚", "那", "简", "饶", "空",
            "曾", "母", "沙", "乜", "养", "鞠", "须", "丰", "巢", "关", "蒯", "相", "查", "后", "荆", "红",
            "游", "竺", "权", "逯", "盖", "益", "桓", "公", "商", "牟", "佘", "佴", "伯", "赏",
            "墨", "哈", "谯", "笪", "年", "爱", "阳", "佟", "言", "福", "微", "生", "岳", "帅", "缑", "亢",
            "况", "后", "有", "琴", "晋", "楚", "闫", "法", "汝", "鄢", "涂", "钦",
            "万俟", "司马", "上官", "欧阳", "夏侯", "诸葛", "南宫",
            "闻人", "东方", "赫连", "皇甫", "尉迟", "公羊", "澹台", "公冶", "宗政", "濮阳", "淳于", "单于",
            "太叔", "申屠", "公孙", "仲孙", "轩辕", "令狐", "钟离", "宇文", "长孙", "慕容", "鲜于", "闾丘",
            "司徒", "司空", "亓官", "司寇", "仉督", "子车", "颛孙", "端木", "巫马", "公西", "漆雕", "乐正",
            "壤驷", "公良", "拓跋", "夹谷", "宰父", "榖梁",
            "段干", "百里", "东郭", "南门", "呼延", "归", "海", "羊舌", "梁丘", "左丘", "东门", "西门", "第五"
    );
    private String name;
    private int failCount = 0;

    private Application hostApp;
    private Method showSlideAcceptDialog;
    private Method showQQToastInUiThreadMethod;
    private ClassLoader classLoader;


    /* ----------------------------------------------------------------------------------- */


    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws ReflectiveOperationException {

        if (!Objects.equals(lpparam.packageName, "com.tencent.mobileqq")) {
            return;
        }


        Class<?> cBaseApplicationImpl = lpparam.classLoader.loadClass("com.tencent.common.app.BaseApplicationImpl");

        /* onCreate#com.tencent.common.app.BaseApplicationImpl */
        XposedHelpers.findAndHookMethod(cBaseApplicationImpl,
            "onCreate",
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws ClassNotFoundException {
                    hostApp = (Application) param.thisObject;
                    classLoader = hostApp.getClassLoader();
                    init();
                    doHook();
                }
            });


    }

    private void init() {
        Logger.i("LegacyHookEntry -> init");
        try {
            Class<?> QQToastUtilClass = classLoader.loadClass("com.tencent.util.QQToastUtil");
            showQQToastInUiThreadMethod = QQToastUtilClass.getDeclaredMethod("showQQToastInUiThread", int.class, String.class);
            Class<?> QQCustomDialogClass = classLoader.loadClass("com.tencent.mobileqq.utils.DialogUtil");
            showSlideAcceptDialog = QQCustomDialogClass.getDeclaredMethod(
                    "showSlideAcceptDialog",
                    Activity.class,
                    String.class,
                    String.class,
                    DialogInterface.OnClickListener.class,
                    DialogInterface.OnClickListener.class,
                    DialogInterface.OnCancelListener.class);
        } catch (Exception e) {
            Logger.e(e);
        }

    }

    private void doHook() throws ClassNotFoundException {
        Logger.i("LegacyHookEntry -> doHook(load class)");
        Class<?> cRetryAbility = classLoader.loadClass("com.tenpay.sdk.net.core.request.RetryAbility");
        Class<?> cFunction0 = classLoader.loadClass("kotlin.jvm.functions.Function0");
        Class<?> cSessionKey = classLoader.loadClass("com.tenpay.sdk.net.core.comm.SessionKey");
        Class<?> cStatisticInfo = classLoader.loadClass("com.tenpay.sdk.net.core.statistic.StatisticInfo");
        Class<?> cPayActivity = classLoader.loadClass("com.tenpay.sdk.activity.PayActivity");
        Class<?> cConfirmRequestAction = classLoader.loadClass("com.tenpay.sdk.net.core.actions.ConfirmRequestAction");
        Class<?> cEncryptProcessor = classLoader.loadClass("com.tenpay.sdk.net.core.processor.EncryptProcessor");
        Class<?> cNetResult = classLoader.loadClass("com.tenpay.sdk.net.core.result.NetResult");

        Logger.i("=----------------- doHook -----------------=");


        /////// onCreateView#com.tenpay.sdk.activity.PayActivity ///////
        XposedHelpers.findAndHookMethod(cPayActivity, "onCreateView", android.view.LayoutInflater.class, android.view.ViewGroup.class, android.os.Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Context context = ((ViewGroup) param.args[1]).getContext();
                        doOnStop(context);

                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                });


        /////// fillNameRequestAction#com.tenpay.sdk.net.core.actions.ConfirmRequestAction ///////
        XposedHelpers.findAndHookMethod(cConfirmRequestAction, "fillNameRequestAction", android.content.Context.class, java.lang.String.class, org.json.JSONObject.class, cRetryAbility, cFunction0, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Logger.i("ConfirmRequestAction -> on beforeHookedMethod");
                start_flag = true;
                stop_flag = false;
                tried_surname_list.clear();

//                Context context = (Context) param.args[0];
                String string = (String) param.args[1];
                JSONObject jsonObject = (JSONObject) param.args[2];
                Object retryAbility = param.args[3];
                Object function0 = param.args[4];

                new Thread(() -> {
                    failCount = 0;
                    try {
                        for (String surname : lst_surname) {
                            if (stop_flag) {
                                Logger.w("stop!! reason: stop_flag==true");
                                return;
                            }
                            Method retryMethod = retryAbility.getClass().getMethod("retry", Map.class);
                            Method functionMethod = function0.getClass().getMethod("invoke");
                            Map<String, String> extraMap = new HashMap<>();
                            extraMap.put("step", "2");
                            extraMap.put("fillName", surname);
                            Logger.w("fillName: " + surname);
                            Toast(surname, 0);

                            functionMethod.invoke(function0);
                            retryMethod.invoke(retryAbility, extraMap);
                            Thread.sleep(200);
                        }
                        Toast("跑完了", 1);
                    } catch (NoSuchMethodException | IllegalAccessException |
                             InvocationTargetException e) {
                        Logger.e("The method 'retry' does not exist.");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

                Logger.d("string: " + string);
                Logger.d("jsonObject: " + jsonObject.toString());
                Logger.d("retryAbility: " + retryAbility.toString());
                Logger.d("function0: " + function0.toString());


                name = string;
                Toast("(?)"+name, 0);

                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });

        /////// encryptExtra#com.tenpay.sdk.net.core.processor.EncryptProcessor ///////
        XposedHelpers.findAndHookMethod(cEncryptProcessor, "encryptExtra",
                java.lang.String.class,
                cSessionKey,
                boolean.class,
                boolean.class,
                java.util.Map.class, java.util.Map.class, java.util.Map.class, cStatisticInfo, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Map<String, String> processResult = (Map<String, String>) param.args[5];

                        String strProcessResult = processResult.toString();
                        Logger.d("before encrypt processResult:" + strProcessResult);
                        if (!start_flag) {
                            return;
                        }

                        String encodedFillName = processResult.get("fillName");
                        if (encodedFillName != null) {
                            String decodedFillName = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                decodedFillName = URLDecoder.decode(encodedFillName, StandardCharsets.UTF_8);
                            }
                            tried_surname_list.add(decodedFillName);
                        }


                        if (strProcessResult.contains("token_id=") && strProcessResult.contains("app_info=") && strProcessResult.contains("model_xml=")) {
                            stop_flag = true;
                            Logger.w("结束");
                        }
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                    }
                });


        /////// setBizResponse#com.tenpay.sdk.net.core.result.NetResult ///////
        XposedHelpers.findAndHookMethod(cNetResult, "setBizResponse", java.lang.Object.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Logger.i("NetResult -> on afterHookedMethod");
                Object obj = param.args[0];
                Logger.d("NetResult - obj ->"+ obj);
                try {
                    JSONObject jsonObject = (JSONObject) obj;
                    if (jsonObject.getInt("retcode") == 66217329){
                        failCount++;
                    }
                } catch (Exception ignored) {}
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });

        Logger.i("^----------------- over -----------------^");
    }


    private void doOnStop(Context context) throws InvocationTargetException, IllegalAccessException {
        Logger.d("LegacyHookEntry - doOnStop!! tried_surname_list=" + tried_surname_list);
        if (!tried_surname_list.isEmpty()) {
            Logger.d("failCount="+failCount);
            String lastElement = tried_surname_list.get(failCount);

            DialogInterface.OnClickListener positiveListener = (dialog, which) -> {};
            DialogInterface.OnCancelListener cancelListener = dialog -> {};
            DialogInterface.OnClickListener negativeListener = (dialog, which) -> {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", lastElement);
                clipboard.setPrimaryClip(clip);
                Toast("复制成功", 2);
            };
            Dialog dialog = (Dialog) showSlideAcceptDialog.invoke(null, context,
                    "TA可能是：" + lastElement+name,
                    "尝试列表:\n" + tried_surname_list,
                    positiveListener, negativeListener, cancelListener);
            assert dialog != null;
            try {
                setupDialogFont(dialog);
                dialog.show();
            } catch (Exception e) {
                Logger.e(e);
            }
        }
    }

    /* ----------------------------------------------------------------------------------- */

    public void Toast(String content, int type) {
        SyncUtils.runOnUiThread(() -> {
            try {
                showQQToastInUiThreadMethod.invoke(null, type, content);
            } catch (Throwable e) {
                Logger.e(e);
            }
        });

    }

    private static void setupDialogFont(Dialog dialog) {
        Window window = dialog.getWindow();
        assert window != null;
        View view = window.getDecorView();
        doSetupDialogViewFont(view, 15);
    }

    private static void doSetupDialogViewFont(View view, int size) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                doSetupDialogViewFont(parent.getChildAt(i), size);
            }
        } else if (view instanceof TextView) {
            TextView textview = (TextView) view;
            String text = textview.getText().toString();

            textview.setTextIsSelectable(true);
            textview.setTextSize(size);

            if (text.equals("取消")) {
                textview.setText("关闭");
            } else if (text.equals("确定")) {
                textview.setText("复制");
            }
        }
    }

    /* ----------------------------------------------------------------------------------- */

    @Override
    public void initZygote(StartupParam startupParam) {}

}
