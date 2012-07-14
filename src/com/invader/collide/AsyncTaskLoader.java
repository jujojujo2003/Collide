//    Copyright 2012 S.Lakshminarayanan (www.s-ln.in)
//    This file is part of Collide.
//
//    Collide is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Collide is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Collide.  If not, see <http://www.gnu.org/licenses/>.
package com.invader.collide;
import android.os.AsyncTask;
 
public class AsyncTaskLoader extends AsyncTask<IAsyncCallback, Integer, Boolean> {
 
    IAsyncCallback[] _params;
    @Override
    protected Boolean doInBackground(IAsyncCallback... params) {
        this._params = params;
        int count = params.length;
        for(int i = 0; i < count; i++){
            params[i].workToDo();
        }
        return true;
    }
 
    @Override
    protected void onPostExecute(Boolean result) {
        int count = this._params.length;
        for(int i = 0; i < count; i++){
            this._params[i].onComplete();
        }
    }
}