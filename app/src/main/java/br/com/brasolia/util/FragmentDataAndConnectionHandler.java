package br.com.brasolia.util;

/**
 * Created by cayke on 13/12/16.
 */

/*
COMO UTILIZAR O FRAGMENTDATAANDCONNECTIONHANDLER:

- ADICIONAR AS VARIAVEIS ABAIXO NA CLASSE
	    private FragmentDataAndConnectionHandler dataAndConnectionHandler;
    	private boolean isLoading = false;
    	//private LayoutInflater inflater; //adicionar caso a tela deseje usar a emptyView

- ADICIONAR NO ONCREATEVIEW()
		        //-------------------- INIT CONNECTION AND EMPTY DATA HANDLER -------------------------------
		        dataAndConnectionHandler = new FragmentDataAndConnectionHandler(inflater, container, rootView);
		        dataAndConnectionHandler.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {
		                FAZER A REQUEST
		                dataAndConnectionHandler.showLoadingLayout();
		            }
		        });
		        dataAndConnectionHandler.setLoadingMessage(MENSAGEM DE LOADING);
       			 //----------------------------------------------------------------------------------

- ADICIONAR O METODO
			@Override
		    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		        super.onActivityCreated(savedInstanceState);

		        if (isLoading) {
		            dataAndConnectionHandler.showLoadingLayout();
		        }
		    }

- FAZER OS TRATAMENTOS NECESSARIO NO METODO DE REQUEST DA TELA
	- tratar os isLoading
	- fazer as chamadas corretas em caso de sucesso, erro, falha retrofit
*/

/*
    OBS BUG: essa classe pode bugar se a mainView for do tipo SwipeRefreshLayout. Nesse caso, utilize um Linear ou Relative como parent.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.com.brasolia.R;

public class FragmentDataAndConnectionHandler {
    private LayoutInflater inflater;
    private ViewGroup rootView;
    private View mainView;
    private View.OnClickListener onClickListener;
    private String loadingMessage;

    public FragmentDataAndConnectionHandler (LayoutInflater inflater, ViewGroup rootView, View mainView) {
        this.inflater = inflater;
        this.rootView = rootView;
        this.mainView = mainView;
    }

    public void showLoadingLayout(){
        View view = changeLayout(R.layout.exception_loading_content);
        if (loadingMessage != null) {
            TextView textView = (TextView) view.findViewById(R.id.exception_message);
            textView.setText(loadingMessage);
        }
    }

    public void showInternetOffLayout(){
        View view = changeLayout(R.layout.exception_no_internet);
        if (onClickListener != null) {
            Button button = (Button) view.findViewById(R.id.exception_button);
            button.setOnClickListener(onClickListener);
        }
    }

    public void showExceptionLayout(){
        View view = changeLayout(R.layout.exception_failure);
        if (onClickListener != null) {
            Button button = (Button) view.findViewById(R.id.exception_button);
            button.setOnClickListener(onClickListener);
        }
    }

    public void showNoDataCustomView(View noDataView){
        changeLayout(noDataView);
    }

    public void showMainView (){
        changeLayout(mainView);
    }

    public void setOnClickListener (View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setLoadingMessage (String message) {
        this.loadingMessage = message;
    }

    private View changeLayout (int layoutID) {
        rootView.removeAllViews();
        return inflater.inflate(layoutID, rootView, true);
    }

    private void changeLayout (View view) {
        int index = rootView.indexOfChild(view);
        if(index < 0) {
            rootView.removeAllViews();
            rootView.addView(view);
        }
    }
}
