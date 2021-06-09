package com.example.satushyzerdeapp.ui.bastybet;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.satushyzerdeapp.R;
import com.example.satushyzerdeapp.activities.LoginActivity;
import com.example.satushyzerdeapp.activities.ProfileActivity;
import com.example.satushyzerdeapp.adapters.HorizontalRecyclerViewAdapter;
import com.example.satushyzerdeapp.adapters.TovarListAdapter;
import com.example.satushyzerdeapp.modules.Tovar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;

public class BastyBetFragment extends Fragment implements View.OnClickListener {
    View view;
    ImageView imgFilterView, imgAccountBtn;
    SearchView searchView;
    Button btnAddTovar;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Tovar> tovarArrayList;
    TovarListAdapter tovarListAdapter;
    ProgressDialog progressDialog;
    AlertDialog.Builder dialog;

    private DatabaseReference mDatabaseReference;
    private StorageReference mstorageReference;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imgUri;
    CardView cardView;
    ImageView imgTovar;
    EditText etTovarName, etTovarPrice, etTovarQuantity, etTovarCode;
    PopupMenu popupMenu;

    ArrayList<Uri> uri;
    RecyclerView recyclerView1;
    HorizontalRecyclerViewAdapter horizontalRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_basty_bet, container, false);
        imgFilterView = view.findViewById(R.id.filterImgView);
        imgAccountBtn = view.findViewById(R.id.accountImgView);
        searchView = view.findViewById(R.id.searchView);
        btnAddTovar = view.findViewById(R.id.btnAddTovar);

        recyclerView = view.findViewById(R.id.recyclerBastyBet);
        tovarArrayList = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("TovarInfo");
        mstorageReference = FirebaseStorage.getInstance().getReference("TovarImages");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Мәліметтер оқылуда...");
        progressDialog.setMessage("Күте тұрыңыз");

        getData();
        initFilter();

        tovarListAdapter = new TovarListAdapter(tovarArrayList, getContext());
        recyclerView.setAdapter(tovarListAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        imgAccountBtn.setOnClickListener(this);
        imgFilterView.setOnClickListener(this);
        btnAddTovar.setOnClickListener(this);
        return view;
    }

    private void searchData() {
        //Log.i("constraint.length()", "tovarArrayList: 1 "+tovarArrayList);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tovarListAdapter.getFilter().filter(newText.toString());
                if (TextUtils.isEmpty(newText)) {

                    //Log.i("constraint.length()", "tovarArrayList: 2 "+tovarArrayList);
                    tovarListAdapter = new TovarListAdapter(tovarArrayList, getContext());
                    recyclerView.setAdapter(tovarListAdapter);
                    tovarListAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        super.onDestroy();
    }

    private void getData() {
        progressDialog.show();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    tovarArrayList.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        progressDialog.cancel();
                        Tovar tovar = snap.getValue(Tovar.class);
                        tovarArrayList.add(tovar);
                    }

                    tovarListAdapter.notifyDataSetChanged();
                    searchData();
                }else {
                    Toast.makeText(getActivity(), "Тауар енгізіңіз!", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void initFilter(){

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accountImgView:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(getContext(), ProfileActivity.class));
                } else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                break;

            case R.id.filterImgView:

                popupMenu = new PopupMenu(getContext(), v);

                popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.name_sort:
                                Collections.sort(tovarArrayList, Tovar.tovarNameComparator);
                                tovarListAdapter.notifyDataSetChanged();
                                break;
                            case R.id.price_sort:
                                Collections.sort(tovarArrayList, Tovar.tovarPriceComparator);
                                tovarListAdapter.notifyDataSetChanged();
                                break;
                            case R.id.new_sort:
                                Toast.makeText(getContext(), "New Sort", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.sale_sort:
                                Collections.sort(tovarArrayList, Tovar.tovarSatylym);
                                tovarListAdapter.notifyDataSetChanged();
                                break;
                            case R.id.available_sort:
                                Collections.sort(tovarArrayList, Tovar.tovarQuanityAvailabilityComparator);
                                tovarListAdapter.notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                break;
            case R.id.btnAddTovar:
                openFileChooser();

                dialog = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.dialog_add_new_tovar, null);

                Button btnAddNewTovar;

                recyclerView1 = dialogView.findViewById(R.id.recyclerview1);
                uri = new ArrayList<>();
                horizontalRecyclerViewAdapter = new HorizontalRecyclerViewAdapter(uri);
                recyclerView1.setLayoutManager(new LinearLayoutManager(dialog.getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView1.setAdapter(horizontalRecyclerViewAdapter);

                etTovarName = dialogView.findViewById(R.id.etTovarName);
                etTovarPrice = dialogView.findViewById(R.id.etTovarPrice);
                etTovarQuantity = dialogView.findViewById(R.id.etTovarQuantity);
                etTovarCode = dialogView.findViewById(R.id.etTovarCode);

                btnAddNewTovar = dialogView.findViewById(R.id.btnAddNewTovar);

//                imgTovar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.i("image", "123 ");
////                        openFileChooser();
//                    }
//                });
//                cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.i("image", "456 ");
////                        openFileChooser();
//                    }
//                });

                btnAddNewTovar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etTovarName.getText().toString().isEmpty()) {
                            etTovarName.setError("Толығымен толтырыңыз!");
                            return;
                        }
                        if (etTovarPrice.getText().toString().isEmpty()) {
                            etTovarPrice.setError("Толығымен толтырыңыз!");
                            return;
                        }
                        if (etTovarQuantity.getText().toString().isEmpty()) {
                            etTovarQuantity.setError("Толығымен толтырыңыз!");
                            return;
                        }
                        if (etTovarCode.getText().toString().isEmpty()) {
                            etTovarCode.setError("Толығымен толтырыңыз!");
                            return;
                        }

                        uploadFile2();
                    }
                });

                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.show();
                break;
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    ProgressDialog progressDialog1;
    ArrayList<String> urlStrings;

    private void uploadFile2() {
//        Log.i("images_uri", "uri.size() " + uri.size());

        progressDialog1 = new ProgressDialog(getContext());
        progressDialog1.setTitle("Енгізілуде...");
        progressDialog1.show();

        if (uri != null && uri.size() != 0) {
            for (int i = 0; i < uri.size(); i++) {

                Uri IndividualImage = uri.get(i);
                final StorageReference fileReference = mstorageReference.child("Images" + IndividualImage.getLastPathSegment());
                urlStrings = new ArrayList<>();

                fileReference.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri3) {
                                urlStrings.add(String.valueOf(uri3));
//                                Log.i("images_uri", "urlStrings.size(): " + urlStrings.size());
//                                Log.i("images_uri", "uri.size(): " + uri.size());

                                if (urlStrings.size() == uri.size()) {
                                    addData();
                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog1.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog1.setMessage("Енгізілді " + (int) progress + "%");
                    }
                });
            }
        } else {
            Toast.makeText(getContext(), "Суретті таңдаңыз!", Toast.LENGTH_SHORT).show();
        }

    }

    public void addData() {
        //Log.i("images_uri", "add Data");
        String imageUris = "";

        for (int i = 0; i < urlStrings.size(); i++) {
            imageUris = imageUris + urlStrings.get(i) + ",";
        }


        Tovar tovar = new Tovar(
                etTovarName.getText().toString(),
                etTovarCode.getText().toString(),
                imageUris,
                Long.parseLong(etTovarPrice.getText().toString()),
                Integer.parseInt(etTovarQuantity.getText().toString()), 0);

        String tovarCode = etTovarCode.getText().toString();

        mDatabaseReference.child(tovarCode).setValue(tovar);
        Toast.makeText(getContext(), " Тауар енгізілді!", Toast.LENGTH_SHORT).show();
        progressDialog1.dismiss();
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        uri.add(data.getClipData().getItemAt(i).getUri());
                        Log.i("count", "123 " + count);
                    }
                } else if (data.getData() != null) {
                    //String imagePath = data.getData().getPath();
                    imgUri = data.getData();
                    uri.add(imgUri);
                }
                horizontalRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }
}