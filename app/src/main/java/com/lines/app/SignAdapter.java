package com.lines.app;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.SignViewHolder> {
    public interface OnSignLongClickListener {
        void onSignLongClick(int position);
    }
    private OnSignLongClickListener listener;
    List<SignData> signDataList;
    public SignAdapter(List<SignData> signDataList, OnSignLongClickListener listener) {
        this.signDataList = signDataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sign, parent, false);
        return new SignViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return signDataList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SignViewHolder holder, int position) {
        SignData signData = signDataList.get(position);

        holder.blueSquare.setText(signData.LineNumber);
        holder.topWhite.setText(signData.Station);
        holder.row1.setText(signData.Row1);
        holder.row2.setText(signData.Row2);
        holder.row3.setText(signData.Row3);
        holder.row4.setText(signData.Row4);
        holder.row5.setText(signData.Row5);

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onSignLongClick(position);
            }
            return true;
        });
    }
    public static class SignViewHolder extends RecyclerView.ViewHolder {

        // Define your TextViews here
        TextView blueSquare, topWhite;
        TextView row1, row2, row3, row4, row5;


        public SignViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find IDs here
            blueSquare = itemView.findViewById(R.id.blueSquare);
            topWhite = itemView.findViewById(R.id.top_white);
            row1 = itemView.findViewById(R.id.row1);
            row2 = itemView.findViewById(R.id.row2);
            row3 = itemView.findViewById(R.id.row3);
            row4 = itemView.findViewById(R.id.row4);
            row5 = itemView.findViewById(R.id.row5);
        }
    }
}
