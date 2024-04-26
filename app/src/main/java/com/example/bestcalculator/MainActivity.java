package com.example.bestcalculator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {
    TextView number1, number2, number3, number4, number5, number6, number7, number8, number9,
             number0, result, equals, expr, point, addition, multiplication, division, minus,
             clear, backSpace, btnAns;

    double lastAns = Double.NaN;
    boolean isOperator = false, ansPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
        number5 = findViewById(R.id.number5);
        number6 = findViewById(R.id.number6);
        number7 = findViewById(R.id.number7);
        number8 = findViewById(R.id.number8);
        number9 = findViewById(R.id.number9);
        number0 = findViewById(R.id.number0);
        expr = findViewById(R.id.expr);
        result = findViewById(R.id.result);
        equals = findViewById(R.id.equals);
        addition = findViewById(R.id.addition);
        minus = findViewById(R.id.minus);
        multiplication = findViewById(R.id.multiplication);
        division = findViewById(R.id.division);
        point = findViewById(R.id.point);
        clear = findViewById(R.id.clear);
        backSpace = findViewById(R.id.backSpace);
        btnAns = findViewById(R.id.lastAns);

        number1.setOnClickListener(this::numbers);
        number2.setOnClickListener(this::numbers);
        number3.setOnClickListener(this::numbers);
        number4.setOnClickListener(this::numbers);
        number5.setOnClickListener(this::numbers);
        number6.setOnClickListener(this::numbers);
        number7.setOnClickListener(this::numbers);
        number8.setOnClickListener(this::numbers);
        number9.setOnClickListener(this::numbers);
        number0.setOnClickListener(this::numbers);



        addition.setOnClickListener(this::operator);
        minus.setOnClickListener(this::operator);
        division.setOnClickListener(this::operator);
        multiplication.setOnClickListener(this::operator);
        point.setOnClickListener(v -> {
            if(checkForPoint())expr.setText(expr.getText() + ".");
        });

        clear.setOnClickListener(v -> {
                    expr.setText("");
                    ansPressed = false;
                    isOperator = false;
                });

        equals.setOnClickListener(v -> evaluateExpression(expr.getText()));
        backSpace.setOnClickListener(this::performBackSpace);
        btnAns.setOnClickListener(this::insertAns);



    }


    void numbers(View view){
        int viewId =view.getId();
        String textId = getResources().getResourceEntryName(viewId);
        String temp = expr.getText() + textId.substring(textId.length()-1);
        expr.setText(temp);
        if(!isOperator) isOperator = true;
    }

    void operator(View view){
        TextView textView = (TextView) view;
        if(!isOperator) return;
        String customData = (String) textView.getTag();
        String existantText = expr.getText() + customData;
        expr.setText(existantText);
        ansPressed = false;
        isOperator = false;
    }
    void evaluateExpression(CharSequence expressionSequence) {
        try {
            if(!isOperator) return;
            String expressionString = (String) expressionSequence;
            expressionString = expressionString.replace('x','*');
            expressionString = expressionString.replace('÷','/');
            Expression expression = new ExpressionBuilder(expressionString).build();
            lastAns = expression.evaluate();
            String temp = String.valueOf(lastAns);
            if(temp.endsWith(".0")) temp = temp.substring(0,temp.length()-2);
            result.setText(temp);
        } catch (ArithmeticException e) {
            // Handle division by zero gracefully
            Log.e("Evaluation Error", "Division by zero occurred");
            showToast("Division by zero");
        }
    }

    void performBackSpace(View  view){
        String currentExpression = (String) expr.getText();
        if(currentExpression.isEmpty()) return;
        currentExpression = currentExpression.substring(0,currentExpression.length()-1);
        expr.setText(currentExpression);
        if(currentExpression.isEmpty()){
            ansPressed = false;
            isOperator = false;
        }else {
            checkLastChar();
        }
    }

    void insertAns(View view){
        checkLastChar();
        if(ansPressed || Double.isNaN(lastAns)) return;
        String temp = String.valueOf(lastAns);
        if(temp.endsWith(".0")) temp = temp.substring(0,temp.length()-2);
        temp = expr.getText().toString() + temp;
        expr.setText(temp);
        ansPressed = true;
        isOperator = true;
    }

    void checkLastChar(){
        String currentExpression = (String) expr.getText();
        if(currentExpression.isEmpty())return;
        char lastCh = currentExpression.charAt(currentExpression.length()-1);
        boolean isDigit = Character.isDigit(lastCh) || lastCh == '.';
        isOperator = ansPressed = isDigit;
    }

    boolean checkForPoint(){
        String currentExpression = (String) expr.getText();
        currentExpression = currentExpression.replace('÷','/');
        if(currentExpression.isEmpty()) return false;
        boolean canBePoint = true, notAnOperator = true;
        int i = currentExpression.length() - 1;
        char lastCh = currentExpression.charAt(i);
        if(lastCh == '+' || lastCh == '/' || lastCh == 'x' || lastCh == '-') return false;

        while (canBePoint && notAnOperator && i>=0){
            lastCh = currentExpression.charAt(i);
            if(lastCh == '+' || lastCh == '/' || lastCh == 'x' || lastCh == '-') notAnOperator = false;
            if(lastCh == '.') canBePoint = false;
            i--;
        }
        System.out.println("not operator = " + notAnOperator);
        System.out.println("i =" + i);
        return canBePoint ;

    }

    void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}