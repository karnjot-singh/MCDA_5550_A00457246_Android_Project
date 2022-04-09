package com.example.hotelreservationsystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class HotelSearchFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    View view;
    EditText checkInEditText, checkOutEditText, guestName, noOfGuests;
    TextView checkInErr, checkOutErr, guestNameErr, noOfGuestsErr;
    Boolean checkInSelected, isSearched;
    Button searchButton, resetButton, viewButton;
    SharedPreferences searchData;
    public static final String myPreference = "myPref";
    public static final String guestNameKey = "guestNameKey";
    public static final String guestCountKey = "guestCountKey";
    public static final String checkInKey = "checkInKey";
    public static final String checkOutKey = "checkOutKey";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hotel_search_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkInEditText = view.findViewById(R.id.checkin_edit_text);
        checkOutEditText = view.findViewById(R.id.checkout_edit_text);
        guestName = view.findViewById(R.id.guest_name_edit_text);
        noOfGuests = view.findViewById(R.id.no_of_guests_edit_text);

        checkInErr = view.findViewById(R.id.check_in_error);
        checkOutErr = view.findViewById(R.id.check_out_error);
        guestNameErr = view.findViewById(R.id.guest_name_error);
        noOfGuestsErr = view.findViewById(R.id.guest_number_error);

        checkInEditText.setInputType(InputType.TYPE_NULL);
        checkOutEditText.setInputType(InputType.TYPE_NULL);

        resetButton = view.findViewById(R.id.reset_button);
        searchButton = view.findViewById(R.id.search_button);
        viewButton = view.findViewById(R.id.view_search_button);

        searchData = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);

        if (searchData.contains(guestNameKey)) {
            guestName.setText(searchData.getString(guestNameKey, ""));
        }
        if (searchData.contains(guestCountKey)) {
            noOfGuests.setText(searchData.getString(guestCountKey, ""));
        }
        if (searchData.contains(checkInKey)) {
            checkInEditText.setText(searchData.getString(checkInKey, ""));
        }
        if (searchData.contains(checkOutKey)) {
            checkOutEditText.setText(searchData.getString(checkOutKey, ""));
        }

        isSearched = false;

        checkInEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInSelected = true;
                showDatePickerDialog(System.currentTimeMillis()-1000);
            }
        });

        checkOutEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInSelected = false;
                showDatePickerDialog(System.currentTimeMillis() + DateUtils.DAY_IN_MILLIS);
            }
        });

        guestName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                if(isSearched) { validateGuestName(); }
            }
        });

        noOfGuests.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                if(isSearched) { validateNoOfGuests(); }
            }
        });

        checkOutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                if(isSearched) { validateCheckOut(); }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSearched = true;

                boolean isGuestNameValid = validateGuestName();
                boolean isNoOfGuestsValid = validateNoOfGuests();
                boolean isCheckOutValid = validateCheckOut();

                if ( isCheckOutValid && isGuestNameValid && isNoOfGuestsValid) {
                    searchData = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = searchData.edit();
                    editor.putString(guestNameKey, guestName.getText().toString());
                    editor.putString(guestCountKey, noOfGuests.getText().toString());
                    editor.putString(checkInKey, checkInEditText.getText().toString());
                    editor.putString(checkOutKey, checkOutEditText.getText().toString());
                    editor.commit();

                    Bundle bundle = new Bundle();

                    bundle.putString("checkInDate", checkInEditText.getText().toString());
                    bundle.putString("checkOutDate", checkOutEditText.getText().toString());
                    bundle.putString("guestNumber", noOfGuests.getText().toString());
                    bundle.putString("guestName", guestName.getText().toString());

                    HotelResultsFragment hotelResultsFragment = new HotelResultsFragment();
                    hotelResultsFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, hotelResultsFragment, "HOTEL_RESULTS");
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    if(getActivity().getCurrentFocus()!=null) {
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    fragmentTransaction.remove(HotelSearchFragment.this);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchData = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
                searchData.edit().clear().commit();

                isSearched = false;
                noOfGuests.setText("");
                noOfGuests.setError(null);
                guestName.setText("");
                guestName.setError(null);
                checkInEditText.setText("");
                checkOutEditText.setText("");
                checkOutEditText.setError(null);
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog infoMessage = new AlertDialog.Builder(getActivity()).create();
                infoMessage.setTitle("Your Selections");
                infoMessage.setMessage(String.format("Check In Date: %s\nCheck Out Date: %s\nName: %s\nGuests: %s",
                                            checkInEditText.getText().toString(),
                                            checkOutEditText.getText().toString(),
                                            guestName.getText().toString(),
                                            noOfGuests.getText().toString()
                                            ));
                infoMessage.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                infoMessage.show();
            }
        });
    }

    private boolean validateGuestName() {
        String text = guestName.getText().toString();
        guestNameErr.setVisibility(View.GONE);

        if (text == null || text.length() == 0) {
            guestNameErr.setText("Please enter your name");
            guestNameErr.setVisibility(View.VISIBLE);
        } else if (!Pattern.matches("^[a-zA-Z ]+$", text)) {
            guestNameErr.setText("Name can contain only alphabets");
            guestNameErr.setVisibility(View.VISIBLE);
        }else if (text.length() < 4) {
            guestNameErr.setText("Name should have at least 4 characters");
            guestNameErr.setVisibility(View.VISIBLE);
        } else {
            return true;
        }
        return false;
    }

    private boolean validateNoOfGuests() {
        String text = noOfGuests.getText().toString();
        noOfGuestsErr.setVisibility(View.GONE);

        if(text == null || text.length() == 0) {
            noOfGuestsErr.setText("Please enter no of guests");
            noOfGuestsErr.setVisibility(View.VISIBLE);
        }
        else if(!Pattern.matches("^[0-9 ]+$",text)) {
            noOfGuestsErr.setText("Please enter only numeric value");
            noOfGuestsErr.setVisibility(View.VISIBLE);
        }
        else if(Integer.parseInt(text)<=0) {
            noOfGuestsErr.setText("At least 1 guest required to make the booking");
            noOfGuestsErr.setVisibility(View.VISIBLE);
        }
        else if(Integer.parseInt(text)>10) {
            noOfGuestsErr.setText("Maximum of 10 guests are allowed");
            noOfGuestsErr.setVisibility(View.VISIBLE);
        }
        else {
            return true;
        }
        return false;
    }

    private boolean validateCheckOut() {
        String checkInText = checkInEditText.getText().toString();
        String checkOutText = checkOutEditText.getText().toString();

        checkInErr.setVisibility(View.GONE);
        checkOutErr.setVisibility(View.GONE);

        if(checkInText != null && checkOutText != null && checkInText.length() > 0 && checkOutText.length() > 0) {
            try {
                String datePattern = "dd MMM yyyy";
                Date checkIn = new SimpleDateFormat(datePattern).parse(checkInEditText.getText().toString());
                Date checkOut = new SimpleDateFormat(datePattern).parse(checkOutEditText.getText().toString());

                if(!checkOut.after(checkIn)) {
                    checkOutErr.setVisibility(View.VISIBLE);
                    checkOutErr.setText("Check out date should be greater than check in Date");
                } else {
                    return true;
                }
            } catch (ParseException e) {
                checkOutErr.setVisibility(View.VISIBLE);
                checkOutErr.setText("Make Sure dates are valid!!");
            }
        }
        else {
            if(checkInText == null || checkInText.length()==0){
                checkInErr.setText("Please select check in date");
                checkInErr.setVisibility(View.VISIBLE);
            }
            if(checkOutText == null || checkOutText.length() == 0) {
                checkOutErr.setText("Please select check out date");
                checkOutErr.setVisibility(View.VISIBLE);
            }
        }

        return false;
    }

    private void showDatePickerDialog(long time) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this.getContext(),
                R.style.DatePicker,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(time);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String strDate = "01 Jan 2023";
        try {
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse((month+1)+"/"+dayOfMonth+"/"+year);
            strDate = new SimpleDateFormat("dd MMM yyyy").format(date);
        } catch (ParseException e) {
            Toast.makeText(getContext(), "INVALID DATE FORMAT", Toast.LENGTH_LONG);
        }

        if(checkInSelected) {
            checkInEditText.setText(strDate);
        } else {
            checkOutEditText.setText(strDate);
        }
    }
}
