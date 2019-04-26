package com.cliqz.browser.controlcenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cliqz.browser.R;
import com.cliqz.browser.telemetry.TelemetryKeys;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ravjit Uppal
 * @author Stefano Pacifici
 */
class  AdBlockListAdapter extends RecyclerView.Adapter<AdBlockListAdapter.ViewHolder> {

    private List<AdBlockDetailsModel> trackerDetails;
    private final boolean isIncognito;
    private final ControlCenterCallback controlCenterCallback;
    private final ControlCenterCallback.TelemetryCallback telemetryCallback;
    private boolean isEnabled = true;
    private @ColorRes int mColorEnabled;
    private @ColorRes int mColorDisabled;

    AdBlockListAdapter(Context context, ControlCenterCallback controlCenterCallback,
                       ControlCenterCallback.TelemetryCallback telemetryCallback,
                       boolean isIncognito) {
        this.trackerDetails = new ArrayList<>();
        this.controlCenterCallback = controlCenterCallback;
        this.telemetryCallback = telemetryCallback;
        this.isIncognito = isIncognito;
        mColorDisabled = getThemeColor(context.getTheme(), R.attr.colorSecondary);
        mColorEnabled = getThemeColor(context.getTheme(), R.attr.colorPrimary);
    }

    @NonNull
    @Override
    public AdBlockListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.tracker_details_list_item, parent, false);
        return new AdBlockListAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AdBlockListAdapter.ViewHolder holder, int position) {
        final AdBlockDetailsModel details = trackerDetails.get(position);
        holder.trackerName.setText(details.companyName);
        holder.trackerCount.setText(Integer.toString(trackerDetails.get(position).adBlockCount));
        holder.infoImage.setColorFilter(isEnabled ? mColorEnabled : mColorDisabled, PorterDuff.Mode.SRC_IN);
        holder.infoImage.setOnClickListener(v -> {
            final int position1 = holder.getAdapterPosition();
            telemetryCallback.sendCCCompanyInfoSignal(position1, TelemetryKeys.ADBLOCK);
            controlCenterCallback.closeControlCenter();
            final String companyName = details.companyName.replaceAll("\\s", "-");
            final String url =
                    String.format("https://cliqz.com/whycliqz/anti-tracking/tracker#%s", companyName);
            controlCenterCallback.openLink(url, isIncognito);
        });
    }

    @Override
    public int getItemCount() {
        return trackerDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView trackerName;
        final TextView trackerCount;
        final ImageView infoImage;

        ViewHolder(View view) {
            super(view);
            trackerName = (TextView) view.findViewById(R.id.tracker_name);
            trackerCount = (TextView) view.findViewById(R.id.tracker_count);
            infoImage = (ImageView) view.findViewById(R.id.info);
        }
    }

    void updateList(List<AdBlockDetailsModel> trackerDetails) {
        this.trackerDetails = trackerDetails;
        notifyDataSetChanged();
    }

    @ColorRes
    private int getThemeColor(Resources.Theme theme, @AttrRes int colorAttr) {
        final TypedValue outValue = new TypedValue();
        theme.resolveAttribute(colorAttr, outValue, true);
        return outValue.data;
    }

    void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
