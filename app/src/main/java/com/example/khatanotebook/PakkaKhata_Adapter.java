package com.example.khatanotebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class PakkaKhata_Adapter extends FirebaseRecyclerAdapter<PakkaKhata_ModelClass, PakkaKhata_Adapter.Manager_ViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public PakkaKhata_Adapter(@NonNull FirebaseRecyclerOptions<PakkaKhata_ModelClass> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull Manager_ViewHolder holder, int position, @NonNull PakkaKhata_ModelClass model) {
        holder.pk_tvName.setText(model.getKhata_name());
        holder.pk_tvDate.setText("Date : "+model.getDate_time());
        holder.pk_address.setText(model.getCity_name()+"/"+model.getArea_name());
        holder.pk_tvTotalBills.setText("Total Bills = "+model.getTotal_Bills());
        holder.pk_tvPendingBills.setText("Pending Bills = "+model.getPending_bills());
        holder.pk_tvPendingPayment.setText("Pending Payments = "+model.getPending_payment());
        holder.pk_tvCellNumber.setText("Phone no # "+model.getPhone_number());
        holder.pk_IDCard.setText("ID Card : "+model.getID_CardNumber());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PakkaKhata_Child_Main.class);
                intent.putExtra("id_no",model.getID_no());
                intent.putExtra("Khata_Name",model.getKhata_name());
                intent.putExtra("Complete_Address",model.getCity_name()+"/"+model.getArea_name()+"/"+model.getStreet_address());
                intent.putExtra("PhoneNumber",model.getPhone_number());
                context.startActivity(intent);
            }
        });
        holder.pk_ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+model.getPhone_number()));
                context.startActivity(intent);

            }
        });
        holder.pk_ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("PakkaKhata").child(getRef(position).getKey())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context,"Khata Deleted Successfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.pk_ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.pk_edit_dialog))
                        .setGravity(Gravity.CENTER)
                        .setMargin(50,0,50,0)
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog.show();
                View view = dialog.getHolderView();
                EditText pk_dialogKhataName,pk_dialogPhoneNumber,pk_dialog_IDcardNumber;
                Button pk_btnDialogUpdate;
                pk_dialogKhataName = view.findViewById(R.id.pk_dialogKhataName);
                pk_dialogPhoneNumber = view.findViewById(R.id.pk_dialogPhoneNumber);
                pk_dialog_IDcardNumber = view.findViewById(R.id.pk_dialog_IDcardNumber);
                pk_btnDialogUpdate = view.findViewById(R.id.pk_btnDialogUpdate);
                pk_dialogKhataName.setText(model.getKhata_name());
                pk_dialogPhoneNumber.setText(model.getPhone_number());
                pk_dialog_IDcardNumber.setText(model.getID_CardNumber());
                pk_btnDialogUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> data_pk = new HashMap<>();
                        data_pk.put("Khata_name",pk_dialogKhataName.getText().toString());
                        data_pk.put("Phone_number",pk_dialogPhoneNumber.getText().toString());
                        data_pk.put("ID_CardNumber",pk_dialog_IDcardNumber.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("PakkaKhata").child(getRef(position).getKey())
                                .updateChildren(data_pk).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context,"Data Updated",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    @NonNull
    @Override
    public Manager_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pakkakhata_recyclerview, parent, false);

        return new Manager_ViewHolder(view);
    }

    public static class Manager_ViewHolder extends RecyclerView.ViewHolder {
        TextView pk_tvName, pk_tvDate, pk_address, pk_tvTotalBills, pk_tvPendingBills, pk_tvPendingPayment, pk_tvCellNumber, pk_IDCard;
        ImageView pk_ivCall,pk_ivDelete,pk_ivEdit;

        public Manager_ViewHolder(@NonNull View itemView) {
            super(itemView);
            pk_tvName = itemView.findViewById(R.id.pk_tvName);
            pk_tvDate = itemView.findViewById(R.id.pk_tvDate);
            pk_address = itemView.findViewById(R.id.pk_address);
            pk_tvTotalBills = itemView.findViewById(R.id.pk_tvTotalBills);
            pk_tvPendingBills = itemView.findViewById(R.id.pk_tvPendingBills);
            pk_tvPendingPayment = itemView.findViewById(R.id.pk_tvPendingPayment);
            pk_tvCellNumber = itemView.findViewById(R.id.pk_tvCellNumber);
            pk_ivCall = itemView.findViewById(R.id.pk_ivCall);
            pk_IDCard = itemView.findViewById(R.id.pk_edIDCard);
            pk_ivDelete = itemView.findViewById(R.id.pk_ivDelete);
            pk_ivEdit = itemView.findViewById(R.id.pk_ivEdit);
        }
    }
}
