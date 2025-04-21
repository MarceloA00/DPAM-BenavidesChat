package com.example.benavideschat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.benavideschat.adapter.ChatRecyclerAdapter;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Message> messageList = new ArrayList<Message>();

    private ArrayList<Question> questions = new ArrayList<Question>();

    private int QuestionIndex = 0;
    private int previousQuestionIndex = 0;

    private String formattedMessage = "";

    ChatRecyclerAdapter adapter;
    EditText MessageInput;
    ImageButton SendBtn;
    RecyclerView ChatBubblesView;
    RelativeLayout TextInput, BottomLayout;
    FlexboxLayout ChoiceInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        MessageInput = findViewById(R.id.message_input);
        SendBtn = findViewById(R.id.message_send_btn);
        ChatBubblesView = findViewById(R.id.chat_recycler_view);
        TextInput = findViewById(R.id.text_input);
        ChoiceInput = findViewById(R.id.choice_input);
        BottomLayout = findViewById(R.id.bottom_layout);

        SendBtn.setOnClickListener(view -> {
            String message = MessageInput.getText().toString().trim();
            Log.d("TEST", "test");
            if (!message.isEmpty()) {
                sendMessage("user", message);
            }
        });

        MessageInput.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                String message = MessageInput.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessage("user", message);
                }
                return true;
            }
            return false;
        });

        setupRecyclerView();
        setQuestions();
        nextQuestion();
    }


    void sendMessage(String sender, String message) {
        messageList.add(new Message(sender, message));
        adapter.notifyItemInserted(messageList.size() - 1);
        ChatBubblesView.smoothScrollToPosition(messageList.size() - 1);
        if (sender.equals("user")) {
            MessageInput.setText("");
            if (questions.get(previousQuestionIndex).getType().equals("int")) {
                if (!isValidNumber(message)) {
                    sendMessage("bot", "Usa un valor numerico por favor. Entre 0 y 120");
                    return;
                }
            }
            formattedMessage = formattedMessage + questions.get(previousQuestionIndex).getExternalMessageFormat()+ " " + message + "\n";
            nextQuestion();
        }
    }

    void setupRecyclerView() {
        adapter = new ChatRecyclerAdapter(messageList);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        ChatBubblesView.setLayoutManager(manager);
        ChatBubblesView.setItemAnimator(new DefaultItemAnimator());
        ChatBubblesView.setAdapter(adapter);
    }

    void nextQuestion(){
        previousQuestionIndex = QuestionIndex;

        if (QuestionIndex >= questions.size()) {
            sendToDoctor();
            return;
        }

        Question currentQuestion = questions.get(QuestionIndex);
        ViewGroup.LayoutParams params = BottomLayout.getLayoutParams();

        if (!currentQuestion.isOpen()) {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            hideKeyboard();
            TextInput.setVisibility(View.GONE);
            ChoiceInput.setVisibility(View.VISIBLE);
            generateButtons(currentQuestion.getAnswers());
        }
        else if (currentQuestion.isOpen() && TextInput.getVisibility() == View.GONE) {
            params.height = dpToPx(80);
            TextInput.setVisibility(View.VISIBLE);
            ChoiceInput.setVisibility(View.GONE);
        }

        BottomLayout.setLayoutParams(params);

        sendMessage("bot", currentQuestion.getQuestion());

        if (currentQuestion.getJumpTo() == -1) {
            QuestionIndex++;
        } else if (currentQuestion.getJumpTo() == -2){
            QuestionIndex = questions.size();
        } else {
            QuestionIndex = currentQuestion.getJumpTo();
        }

    }

    private void generateButtons(String[] options) {
        ChoiceInput.removeAllViews();

        for (String option : options) {
            Button button = new Button(this);
            button.setText(option);
            button.setTextColor(ContextCompat.getColor(this, R.color.my_secondary));
            button.setBackgroundResource(R.drawable.circular_rect);
            button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.my_tertiary));

            // Set layout params with margin
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            int margin = getResources().getDimensionPixelSize(R.dimen.button_margin);
            params.setMargins(0, 0, margin, margin); // right and bottom margin
            button.setLayoutParams(params);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String answer = ((Button) v).getText().toString();
                    sendMessage("user", answer);
                }
            });
            ChoiceInput.addView(button);
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private boolean isValidNumber(String input) {
        try {
            int number = Integer.parseInt(input);
            return number >= 0 && number <= 120;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void sendWhatsAppMessage() {
        String phoneNumber = "+528180856100";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + formattedMessage));
        startActivity(intent);
    }

    private void sendToDoctor() {
        sendMessage("bot", "Presiona el boton inferior para ponerte en contacto:");

        ViewGroup.LayoutParams params = BottomLayout.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        hideKeyboard();
        TextInput.setVisibility(View.GONE);
        ChoiceInput.setVisibility(View.VISIBLE);
        BottomLayout.setLayoutParams(params);
        ChoiceInput.removeAllViews();

        Button button = new Button(this);
        button.setText("Consultar con Doctor");
        button.setTextColor(ContextCompat.getColor(this, R.color.my_secondary));
        button.setBackgroundResource(R.drawable.circular_rect);
        button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.my_tertiary));

        // Set layout params with margin
        FlexboxLayout.LayoutParams flexparams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        int margin = getResources().getDimensionPixelSize(R.dimen.button_margin);
        flexparams.setMargins(0, 0, margin, margin); // right and bottom margin
        button.setLayoutParams(flexparams);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWhatsAppMessage();
            }
        });
        ChoiceInput.addView(button);
    }

    // Open Question template:
    // questions.add(new Question("",true));
    // Closed Question template
    // questions.add(new Question("",false, new String[]{"Answer 1", "Answer 2", "Answer 3"}));
    void setQuestions() {
        questions.add(new Question("¿Cuál es tu nombre?",true, "Nombre:"));
        questions.add(new Question("¿Cuál es tu edad?",true, "Edad:", "int"));
        questions.add(new Question("¿Como te identificas?",false, new String[]{"Mujer", "Hombre", "Otro"}, "Identificacion:"));
        questions.add(new Question("¿Tienes alguna condición médica preexistente? (Diabetes, Hipertensión, Asma, etc.)",true, "Condiciones preesistentes:"));
        questions.add(new Question("¿Estás tomando algún medicamento actualmente?",true, "Usa los medicamentos:"));
        questions.add(new Question("¿Tienes alergias a algún medicamento?",true, "Alergia a los Medicamentos:"));
        questions.add(new Question("¿Qué síntomas estás presentando?",true, "Sintomas:"));
        questions.add(new Question("¿Desde cuándo sientes estos síntomas?",true, "Inicio de Sintomas:"));
        questions.add(new Question("¿El malestar es constante o aparece por momentos?",true, "Frequencia:"));
//        questions.add(new Question("¿Cómo describirías el malestar?",false, new String[]{"Leve", "Moderado", "Fuerte"}, "Nivel del Malestar:"));

        questions.add(new Question("¿Has tenido náuseas, vómito o diarrea?",true, "NVD:"));
        questions.add(new Question("¿Tienes dificultad para respirar?",false, new String[]{"Si", "No"}, "Dificultad al Respirar:"));
        questions.add(new Question("¿Tienes dolor en el pecho?",false, new String[]{"Si", "No"}, "Dolor de Pecho:"));
        questions.add(new Question("¿Has estado en contacto con alguien enfermo recientemente?",false, new String[]{"Si", "No"}, "Contacto con alguien enfermo:"));
        questions.add(new Question("¿Has viajado en los últimos días?",false, new String[]{"Si", "No"}, "Viajado Recientemente:"));

        questions.add(new Question("¿Cómo describirías el malestar?",false, new String[]{"Leve", "Moderado", "Fuerte"}, "Nivel del Malestar:"));
    }
}