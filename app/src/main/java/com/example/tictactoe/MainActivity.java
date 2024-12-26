package com.example.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount = 0;
    private TextView titleText;
    private Button resetButton, newGameButton;
    private String player1Name = "Player 1";
    private String player2Name = "Player 2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Collect player names
        collectPlayerNames();

        titleText = findViewById(R.id.titleText);
        resetButton = findViewById(R.id.resetButton);
        newGameButton = findViewById(R.id.newGameButton);

        // Initialize Grid Buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + ((i * 3) + j + 1);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        // Reset Button Action
        resetButton.setOnClickListener(view -> resetGame());

        // New Game Button Action
        newGameButton.setOnClickListener(view -> {
            collectPlayerNames();
            resetGame();
        });
    }

    private void collectPlayerNames() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_player_names, null);
        EditText player1Input = dialogView.findViewById(R.id.player1Name);
        EditText player2Input = dialogView.findViewById(R.id.player2Name);

        builder.setView(dialogView)
                .setTitle("Enter Player Names")
                .setPositiveButton("Start Game", (dialog, which) -> {
                    player1Name = player1Input.getText().toString().trim();
                    player2Name = player2Input.getText().toString().trim();

                    if (player1Name.isEmpty()) player1Name = "Player 1";
                    if (player2Name.isEmpty()) player2Name = "Player 2";

                    Toast.makeText(this, "Welcome " + player1Name + " and " + player2Name, Toast.LENGTH_SHORT).show();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;

        if (!button.getText().toString().equals("")) {
            return; // Already clicked
        }

        // Mark X or O
        if (player1Turn) {
            button.setText("X");
        } else {
            button.setText("O");
        }

        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                showWinner(player1Name + " Wins!");
            } else {
                showWinner(player2Name + " Wins!");
            }
        } else if (roundCount == 9) {
            showWinner("It's a Draw!");
        } else {
            player1Turn = !player1Turn; // Switch Turn
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        // Check rows, columns, and diagonals
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void showWinner(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage(message)
                .setMessage("\nDo you want to play again or exit?")
                .setCancelable(false)
                .setPositiveButton("New Game", (dialog, which) -> {
                    resetGame(); // Start a new game
                    Toast.makeText(this, "New Game Started!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Exit", (dialog, which) -> {
                    finish(); // Exit the app
                })
                .create()
                .show();
    }


    private void resetGame() {
        roundCount = 0;
        player1Turn = true;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }
}
