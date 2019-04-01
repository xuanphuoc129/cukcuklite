package com.example.lxphuoc.cukcuklite.keyboard;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lxphuoc.cukcuklite.R;
import com.example.lxphuoc.cukcuklite.keyboard.model.InputKeys;
import com.example.lxphuoc.cukcuklite.keyboard.model.Operators;
import com.example.lxphuoc.cukcuklite.utils.CommonUtils;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

/**
 * Màn hình bàn phím nhập đơn vị tính
 * @created_by lxphuoc on 3/28/2019
 */
public class KeyboarFragment extends DialogFragment {


    private static IOnClickDone mIOnClickDone;

    RecyclerView rcvKeyboard;

    KeyboardAdapter mKeyboardAdapter;

    EditText etInputNumber;

    ArrayList<InputKeys> mDatasets;

    ArrayList<Operators> operators;

    ImageView ivButtonCloseKeyboard;

    private String textInput = "";


    public KeyboarFragment() {

    }

    public static KeyboarFragment createInstance(String textInput, IOnClickDone mIOnClickDone) {
        KeyboarFragment keyboarFragment = new KeyboarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("numberColumn", 4);
        bundle.putString("textInput", textInput);
        keyboarFragment.setArguments(bundle);
        keyboarFragment.setOnClickDone(mIOnClickDone);
        return keyboarFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_keyboard, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        createItems();

        mKeyboardAdapter = new KeyboardAdapter(mDatasets);

        setOnClickItem();

        rcvKeyboard = view.findViewById(R.id.rcvKeyboard);
        etInputNumber = view.findViewById(R.id.etInputNumber);
        ivButtonCloseKeyboard = view.findViewById(R.id.ivButtonCloseKeyboard);

        Bundle bundle = getArguments();

        int numberColumn = 4;
        if (bundle != null) {
            if (bundle.containsKey("numberColumn")) {
                numberColumn = bundle.getInt("numberColumn");
            }
            if (bundle.containsKey("textInput")) {
                textInput = bundle.getString("textInput");
                if (textInput != null && textInput.isEmpty()) {
                    etInputNumber.setSelection(etInputNumber.getText().length());
                    etInputNumber.selectAll();
                    return;
                }
                etInputNumber.setText(textInput);
                etInputNumber.setSelection(etInputNumber.getText().length());
                etInputNumber.selectAll();
            }
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), numberColumn);
        rcvKeyboard.setLayoutManager(gridLayoutManager);
        rcvKeyboard.setAdapter(mKeyboardAdapter);

        operators = new ArrayList<>();

        ivButtonCloseKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        etInputNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    hideKeyboard();
                }
            }
        });

    }

    /**
     * Phương thức ẩn bàn phím
     * @created_by lxphuoc on 3/28/2019
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view == null) {
            view = new View(getActivity());
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    

    /**
     * Khởi tạo giá trị cho danh sách bàn phím
     *
     * @created_by lxphuoc on 3/26/2019
     */
    private void createItems() {
        mDatasets = new ArrayList<>();
        String jsonString = CommonUtils.getInstance().loadJSONFromAsset(getContext(), "json/inputkeys.json");

        if (jsonString != null) {
//            Log.d(TAG, "createItems: " + jsonString);
            try {

                JSONObject jsonObject = new JSONObject(jsonString);

//                Log.d(TAG, "createItems: " + jsonObject.toString());

                JSONArray datas = jsonObject.getJSONArray("data");

                for (int i = 0; i < datas.length(); i++) {
                    JSONObject o = (JSONObject) datas.get(i);
//                    Log.d(TAG, "createItems: " + o.toString());
                    mDatasets.add(new InputKeys(o.getInt("id"), o.getString("name")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < 10; i++) {
                mDatasets.add(new InputKeys((i + 1), String.valueOf(i)));
            }
        }
    }

    /**
     * Phương thức gán sự kiện nhập phím trên màn hình
     * @created_by lxphuoc on 3/28/2019
     */
    private void setOnClickItem() {
        mKeyboardAdapter.setOnClickListener(new KeyboardAdapter.OnclickInputKey() {
            @Override
            public void onClickItem(int id) {
                onChangeText(id);
            }


        });
    }

    /**
     * Phương thức xử lí sự kiện nhập giá
     * @param id - mã bàn phím đã nhập
     */
    private void onChangeText(int id) {
        try {
            String text = textInput;
            String textString = etInputNumber.getText().toString();

            switch (id) {
                case 1: // Clear
                    etInputNumber.setText("0");
                    textInput = "";
                    break;
                case 2: // Giảm
                    if (getNumberInput(text) < 1) {
                        return;
                    }
                    addValue(String.valueOf(getNumberInput(text) - 1));
                    break;
                case 3: // Tăng
                    addValue(String.valueOf(getNumberInput(text) + 1));
                    break;
                case 4: // Back space
                    if (textString.length() == 1) {
                        etInputNumber.setText("0");
                        textInput = "";
                    } else {
                        textString = textString.substring(0, textString.length() - 1);
                        etInputNumber.setText(textString);
                        etInputNumber.setSelection(etInputNumber.getText().length());
                    }
                    break;
                case 5: // 7
                    text = text + "7";
                    addValue(text);
                    break;
                case 6: // 8
                    text = text + "8";
                    addValue(text);
                    break;
                case 7: // 9
                    text = text + "9";
                    addValue(text);
                    break;
                case 8: // -
                    if (textString.charAt(textString.length() - 1) == '-') { // Đằng trước là 1 dấu -
                        return;
                    } else if (textString.charAt(textString.length() - 1) == '+') { // Đằng trước là 1 dấu +
                        text = textString.substring(0, textString.length() - 1);
                        text = text + "-";
                        etInputNumber.setText(text);
                        etInputNumber.setSelection(etInputNumber.getText().length());
                        return;
                    } else {
                        operators.add(new Operators(1, getNumberInput(text)));
                        textInput = "";
                        text = textString + "-";
                        etInputNumber.setText(text);
                        etInputNumber.setSelection(etInputNumber.getText().length());

                    }
                    break;
                case 9: // 4
                    text = text + "4";
                    addValue(text);
                    break;
                case 10: // 5
                    text = text + "5";
                    addValue(text);
                    break;
                case 11: // 6
                    text = text + "6";
                    addValue(text);
                    break;
                case 12: // +
                    if (textString.charAt(textString.length() - 1) == '+') { // Đằng trước là 1 dấu +
                        return;
                    } else if (textString.charAt(textString.length() - 1) == '-') { // Đằng trước là 1 dấu -
                        text = textString.substring(0, textString.length() - 1);
                        text = text + "+";
                        etInputNumber.setText(text);
                        etInputNumber.setSelection(etInputNumber.getText().length());
                        return;
                    } else {
                        operators.add(new Operators(2, getNumberInput(text)));
                        textInput = "";
                        text = textString + "+";
                        etInputNumber.setText(text);
                        etInputNumber.setSelection(etInputNumber.getText().length());
                    }
                    break;
                case 13: // 1
                    text = text + "1";
                    addValue(text);
                    break;
                case 14: // 2
                    text = text + "2";
                    addValue(text);
                    break;
                case 15: // 3
                    text = text + "3";
                    addValue(text);
                    break;
                case 16: // +/-
//                    if (text.indexOf("-") == 0) {
//                        text = text.substring(1, text.length());
//                        etInputNumber.setText(text);
//                    } else {
//                        text = "-" + text;
//                        etInputNumber.setText(text);
//                    }
                    break;
                case 17: // 0
                    text = text + "0";
                    addValue(text);
                    break;
                case 18: // 000
                    text = text + "000";
                    addValue(text);
                    break;
                case 19: // ,
//                    if (text.indexOf(".") > 0) { // Đã tồn tại dấu chấm
//                        return;
//                    }
//                    text = text + ".";
//                    etInputNumber.setText(text);
                    break;
                case 20: // Xong
//                    operators.add(new Operators(3, getNumberInput(text)));
                    onCaculate();

                    break;
                default:
                    break;
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức tính giá trị phép tính và kết thúc nhập
     * @created_by lxphuoc on 3/28/2019
     */
    private void onCaculate() {
        if(operators.size() > 0){
            String txt = etInputNumber.getText().toString();
            txt = txt.replaceAll(",", "");
            Expression expression = new ExpressionBuilder(txt).build();
            try {
                // Calculate the result and display
                double result = expression.evaluate();
                textInput = formatAmount((long) result);
                etInputNumber.setText(textInput);
                operators.clear();

                mKeyboardAdapter.onChangelabel();
            } catch (ArithmeticException ex) {
                ex.fillInStackTrace();
            }
        }else{
            long price = getNumberInput(etInputNumber.getText().toString());
            if(price < 0){
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.error_money),Toast.LENGTH_SHORT).show();
            }else{
                mIOnClickDone.onClickDone(price, etInputNumber.getText().toString());
                dismiss();
            }

        }

    }

    /**
     * Phương thức thêm giá trị vào input
     * @param text - Chuỗi đang nhập
     *             @created_by lxphuoc on 3/28/2019
     */
    private void addValue(String text) {
        try {

            textInput = formatAmount(getNumberInput(text));

            if (textInput.length() > 19) {
                return;
            }

            String values = getStringBefore() + textInput;
            etInputNumber.setText(values);
            etInputNumber.setSelection(etInputNumber.getText().length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức giá trị số từ chuỗi đã nhập
     * @param text - Chuỗi đã nhập
     * @return Giá trị số
     * @created_by lxphuoc on 3/28/2019
     */
    private long getNumberInput(String text) {
        try {
            if (text.isEmpty()) {
                return 0;
            }
            text = text.replaceAll("\\,", "");
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Định dạng số thành 1 chuỗi có dấu phẩy ngăn cách
     * @param num - giá trị số muốn đổi
     * @return Chuỗi string đã xử lý
     * @created_by lxphuoc on 3/28/2019
     */
    private String formatAmount(long num) {
        DecimalFormat decimalFormat = new DecimalFormat();
        DecimalFormatSymbols decimalFormateSymbol = new DecimalFormatSymbols();
        decimalFormateSymbol.setGroupingSeparator(',');
        decimalFormat.setDecimalFormatSymbols(decimalFormateSymbol);
        return decimalFormat.format(num);
    }

    /**
     * Chuỗi string đã lưu vào mảng trước toán tử gần nhất
     * @return Chuỗi đã xử lý
     * @created_by lxphuoc on 3/28/2019
     */
    public String getStringBefore() {
        if (operators == null || operators.size() == 0) {
            return "";
        } else {
            StringBuilder text = new StringBuilder();
            for (Operators o : operators) {
                text.append(formatAmount(o.getValue())).append(o.getId() == 1 ? "-" : "+");
            }
            return text.toString();
        }
    }

    /**
     * Phương thức thiết lập sự kiện callback khi người dùng chọn button "Xong"
     * @param mIOnClickDone Sự kiện callback
     *                      @created_by lxphuoc on 3/28/2019
     */
    public void setOnClickDone(KeyboarFragment.IOnClickDone mIOnClickDone){
        KeyboarFragment.mIOnClickDone = mIOnClickDone;
    }
    /**
     * Interface cho sự kiện callback
     */
    public interface IOnClickDone{
        void onClickDone(long price, String priceShow);
    }
}
