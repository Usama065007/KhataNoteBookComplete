package com.example.khatanotebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.SmsManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class PakkaKhata_Child_Adapter extends FirebaseRecyclerAdapter<PakkaKhata_Child_ModelClass, PakkaKhata_Child_Adapter.Manager_ViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    String IDno;
    String Phone_number;
    int total_pending_payment=0;
    int counter = 0;
    public PakkaKhata_Child_Adapter(@NonNull FirebaseRecyclerOptions<PakkaKhata_Child_ModelClass> options,Context context,String IDno,String Phone_number) {
        super(options);
        this.context=context;
        this.IDno=IDno;
        this.Phone_number = Phone_number;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull Manager_ViewHolder holder, int position, @NonNull PakkaKhata_Child_ModelClass model) {
        holder.pk_child_R_BillName.setText(model.getInvoiceName());
        holder.pk_child_R_TotalItems.setText("Total Items = "+model.getTotalItems());
        holder.pk_child_R_TotalAmount.setText("Total Amount = "+model.getTotalAmount());
        holder.pk_child_R_ReceivedAmount.setText("Received Amount = "+model.getAdvanceAmount());
        holder.pk_child_R_Date.setText(model.getDateTime());
        int pendingAmount = Integer.parseInt(model.getTotalAmount()) - Integer.parseInt(model.getAdvanceAmount());
        total_pending_payment = pendingAmount+total_pending_payment;
        holder.pk_child_R_PendingAmount.setText("Pending Amount = "+Integer.toString(pendingAmount));
        FirebaseDatabase.getInstance().getReference().child("PakkaKhata").child(IDno).child("Total_BillsDetail").child(getRef(position).getKey())
                .child("PendingAmount").setValue(Integer.toString(pendingAmount));
        FirebaseDatabase.getInstance().getReference().child("PakkaKhata").child(IDno).child("Pending_payment").setValue(Integer.toString(total_pending_payment));
        if (pendingAmount !=0){
            counter = counter+1;
        }
        FirebaseDatabase.getInstance().getReference().child("PakkaKhata").child(IDno).child("Pending_bills").setValue(Integer.toString(counter));
        holder.pk_child_R_ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.pk_child_edit_dialog))
                        .setGravity(Gravity.CENTER)
                        .setMargin(50,0,50,0)
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog.show();
                View view = dialog.getHolderView();
                EditText dialog_Name, dialog_TotalBill, dialog_advance, dialog_totalitems;
                Button btnDialog_update;
                dialog_Name = view.findViewById(R.id.dialog_Name);
                dialog_TotalBill = view.findViewById(R.id.dialog_TotalBill);
                dialog_advance = view.findViewById(R.id.dialog_advance);
                dialog_totalitems = view.findViewById(R.id.dialog_totalitems);
                btnDialog_update = view.findViewById(R.id.btnDialog_update);
                dialog_Name.setText(model.getInvoiceName());
                dialog_TotalBill.setText(model.getTotalAmount());
                dialog_advance.setText(model.getAdvanceAmount());
                dialog_totalitems.setText(model.getTotalItems());
                btnDialog_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> data_a = new HashMap<>();
                        data_a.put("InvoiceName",dialog_Name.getText().toString());
                        data_a.put("AdvanceAmount",dialog_advance.getText().toString());
                        data_a.put("TotalAmount",dialog_TotalBill.getText().toString());
                        data_a.put("TotalItems",dialog_totalitems.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("PakkaKhata").child(IDno).child("Total_BillsDetail").child(getRef(position).getKey())
                                .updateChildren(data_a).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                int pendingAmount = Integer.parseInt(dialog_TotalBill.getText().toString()) - Integer.parseInt(dialog_advance.getText().toString());
                                FirebaseDatabase.getInstance().getReference().child("PakkaKhata").child(IDno).child("Total_BillsDetail").child(getRef(position).getKey())
                                        .child("PendingAmount").setValue(Integer.toString(pendingAmount));
                                Toast.makeText(context,"Data Updated Successfully",Toast.LENGTH_SHORT).show();
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
        holder.pk_child_R_ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("PakkaKhata").child(IDno).child("Total_BillsDetail").child(getRef(position).getKey())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        holder.pk_child_R_ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager smgr = SmsManager.getDefault();
                String message = "Thanks for Shopping"+
                        "\n\n"+model.getInvoiceName()+"\n"+"Date : "+model.getDateTime()+"\n"+"Total Items = "+model.getTotalItems()+"\n"
                        +"Total Bill = "+model.getTotalAmount()+"\n"+"Advance = "+model.getAdvanceAmount()+"\n"+"Pending Amount = "+model.getPendingAmount()
                        +"\n\n"+"Regards : Abid General Store";
                smgr.sendTextMessage(Phone_number,null,message,null,null);
                Toast.makeText(context,"Message Sent",Toast.LENGTH_SHORT).show();
            }
        });
        FirebaseDatabase.getInstance().getReference().child("PakkaKhata").child(IDno).child("Total_BillsDetail")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int size = (int) snapshot.getChildrenCount();
                        FirebaseDatabase.getInstance().getReference().child("PakkaKhata").child(IDno).child("Total_Bills")
                                .setValue(Integer.toString(size));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @NonNull
    @Override
    public Manager_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pakkakhata_child_recyclerview, parent, false);
        return new Manager_ViewHolder(view);
    }

    public static class Manager_ViewHolder extends RecyclerView.ViewHolder {
        TextView pk_child_R_BillName,pk_child_R_TotalItems, pk_child_R_TotalAmount, pk_child_R_ReceivedAmount,pk_child_R_PendingAmount, pk_child_R_Date;
        ImageView pk_child_R_ivEdit, pk_child_R_ivDelete, pk_child_R_ivMessage;


        public Manager_ViewHolder(@NonNull View itemView) {
            super(itemView);
            pk_child_R_BillName =itemView.findViewById(R.id.pk_child_R_BillName);
            pk_child_R_TotalItems = itemView.findViewById(R.id.pk_child_R_TotalItems);
            pk_child_R_TotalAmount = itemView.findViewById(R.id.pk_child_R_TotalAmount);
            pk_child_R_ReceivedAmount = itemView.findViewById(R.id.pk_child_R_ReceivedAmount);
            pk_child_R_PendingAmount = itemView.findViewById(R.id.pk_child_R_PendingAmount);
            pk_child_R_Date= itemView.findViewById(R.id.pk_child_R_Date);
            pk_child_R_ivEdit= itemView.findViewById(R.id.pk_child_R_ivEdit);
            pk_child_R_ivDelete= itemView.findViewById(R.id.pk_child_R_ivDelete);
            pk_child_R_ivMessage= itemView.findViewById(R.id.pk_child_R_ivMessage);
        }
    }
}
