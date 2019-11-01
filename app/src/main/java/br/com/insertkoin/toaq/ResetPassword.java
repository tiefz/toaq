package br.com.insertkoin.toaq;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.CancellationException;

public class ResetPassword extends BottomSheetDialogFragment {
    private ResetPasswordInterface mReset;
    EditText inputReset;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_forgotpassword, container, false);
        inputReset = v.findViewById(R.id.inputReset);
        Button buttonReset = v.findViewById(R.id.buttonReset);

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) {
                    return;
                }
                mReset.onButtonClicked(inputReset.getText().toString());
                dismiss();
            }
        });
        return v;
    }

    public interface ResetPasswordInterface {
        void onButtonClicked(String text);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = inputReset.getText().toString();
        if (TextUtils.isEmpty(email)) {
            inputReset.setError("Required.");
            valid = false;
        } else {
            inputReset.setError(null);
        }

        return valid;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mReset = (ResetPasswordInterface) context;
        } catch (ClassCastException e) {
            throw new CancellationException(context.toString()
            + " precisa implementar resetpasswordinterface");
        }
    }
}
