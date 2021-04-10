package ua.opu.herhel_ai183_lab3;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactHolder> {
    private static final int EMPTY_LIST_TYPE = 0;
    private static final int NON_EMPTY_LIST_TYPE = 1;
    private final LayoutInflater _inflater;
    private final List<Contact> _contacts;
    private DeleteItemListener _listener;

    public interface DeleteItemListener{
        void  onDeleteItem(int position);
    }

    public ContactsAdapter(Context applicationContext, List<Contact> contacts, DeleteItemListener deleteItemListener) {
        _inflater = LayoutInflater.from(applicationContext);
        _contacts = contacts;
        _listener = deleteItemListener;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == EMPTY_LIST_TYPE) {
            view = _inflater.inflate(R.layout.list_no_items, parent, false);
            view.setTag(EMPTY_LIST_TYPE);
        }
        else {
            view = _inflater.inflate(R.layout.list_contact, parent, false);
            view.setTag(NON_EMPTY_LIST_TYPE);
        }
        return new ContactHolder(view, _listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        if(getItemViewType(position) == EMPTY_LIST_TYPE)
            return;
        Contact contact = _contacts.get(position);
        holder._image.setImageURI(contact.getUri());
        holder._name.setText(contact.getName());
        holder._email.setText(contact.getEmail());
        holder._phone.setText(contact.getPhone());
    }

    @Override
    public int getItemViewType(int position) {
        return _contacts.isEmpty() ? EMPTY_LIST_TYPE : NON_EMPTY_LIST_TYPE;
    }

    @Override
    public int getItemCount() {
        return Math.max(_contacts.size(), 1);
    }

    static class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView _image;
        private TextView _name;
        private TextView _email;
        private TextView _phone;

        private ImageButton _deleteBtn;
        private DeleteItemListener _listener;
        public ContactHolder(@NonNull View itemView, DeleteItemListener listener) {
            super(itemView);

            _image = itemView.findViewById(R.id.contact_image);
            _name = itemView.findViewById(R.id.name);
            _email = itemView.findViewById(R.id.email);
            _phone = itemView.findViewById(R.id.phone);

            _deleteBtn = itemView.findViewById(R.id.clearButton);
            _listener = listener;

            if ((int) itemView.getTag() == NON_EMPTY_LIST_TYPE) {
                _deleteBtn.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            _listener.onDeleteItem(getAdapterPosition());
        }
    }
}
