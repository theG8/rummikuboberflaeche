package de.g8keeper.rummikuboberflaeche;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Tile;

public class TileView extends View {

    private static final String TAG = TileView.class.getSimpleName();

    private Context context;
    private Paint paint = new Paint();


    private Tile mTile = null;



    public TileView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;


        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        paint.setTextAlign(Paint.Align.CENTER);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int backgroundColor = getResources().getColor(R.color.tile_background, null);
        int textColor;
        String valueText;

        paint.setColor(backgroundColor);
        canvas.drawRect(0, 0, getRight(), getBottom(), paint);


        if (mTile != null) {

            if (mTile.isJoker()){
                textColor = ContextCompat.getColor(getContext(), R.color.tile_joker);
                valueText = "J";
            } else {
                textColor = ContextCompat.getColor(getContext(), mTile.getColor().getColorId());
                valueText = Integer.toString(mTile.getValue());
            }
        } else {
            valueText = Integer.toString(Tile.MAX_VALUE);
            textColor = ContextCompat.getColor(getContext(), Color.RED.getColorId());
        }


        Log.d(TAG, "onDraw: valueText: " + valueText + " textColor: " + textColor);

        int i = 1;

        paint.setTextSize(i);

        while (paint.measureText(Integer.toString(Tile.MAX_VALUE)) < (getWidth() /3*2)) {
            paint.setTextSize(++i);
        }

        paint.setColor(textColor);
        canvas.drawText(valueText, getWidth() / 2, getHeight() / 2, paint);

    }



    public Tile getTile() {
        return mTile;
    }

    public void setTile(Tile tile) {
        this.mTile = tile;
        postInvalidate();
    }
}
