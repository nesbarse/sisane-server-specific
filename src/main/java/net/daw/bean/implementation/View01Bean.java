/*
 * Copyright (c) 2016 by Rafael Angel Aznar Aparici (rafaaznar at gmail dot com)
 * 
 * sisane-server: Helps you to develop easily AJAX web applications 
 *                   by copying and modifying this Java Server.
 *
 * Sources at https://github.com/rafaelaznar/sisane-server
 * 
 * sisane-server is distributed under the MIT License (MIT)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.daw.bean.implementation;

import com.google.gson.annotations.Expose;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.daw.bean.publicinterface.GenericBean;
import net.daw.dao.implementation.UserDao;

public class View01Bean implements GenericBean {

    @Expose
    private String etiquetas = "";
    @Expose
    private Integer numetiquetas = 0;
    @Expose(serialize = false)
    private Integer id_usuario = 0; //important zero-initialize foreign keys
    @Expose(deserialize = false)
    private UserBean obj_usuario = null;

    public String getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(String etiquetas) {
        this.etiquetas = etiquetas;
    }

    public Integer getNumetiquetas() {
        return numetiquetas;
    }

    public void setNumetiquetas(Integer numetiquetas) {
        this.numetiquetas = numetiquetas;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public UserBean getObj_usuario() {
        return obj_usuario;
    }

    public void setObj_usuario(UserBean obj_usuario) {
        this.obj_usuario = obj_usuario;
    }

    public String getColumns() {
        String strColumns = "";
        strColumns += "etiquetas,";
        strColumns += "numetiquetas,";
        strColumns += "id_usuario";
        return strColumns;
    }

    @Override
    public String getValues() {
        String strColumns = "";
        strColumns += etiquetas + ",";
        strColumns += numetiquetas + ",";
        strColumns += id_usuario;
        return strColumns;
    }

    @Override
    public String toPairs() {
        String strPairs = "";
        strPairs += "etiquetas=" + etiquetas + ",";
        strPairs += "numetiquetas=" + numetiquetas + ",";
        strPairs += "id_usuario=" + id_usuario;
        return strPairs;
    }

    @Override
    public View01Bean fill(ResultSet oResultSet, Connection pooledConnection, PuserBean oPuserBean_security, Integer expand) throws SQLException, Exception {
        this.setEtiquetas(oResultSet.getString("etiquetas"));
        this.setNumetiquetas(oResultSet.getInt("numetiquetas"));
        if (expand > 0) {
            UserBean oUserBean = new UserBean();
            UserDao oUserDao = new UserDao(pooledConnection, oPuserBean_security);
            oUserBean.setId(oResultSet.getInt("id_usuario"));
            oUserBean = oUserDao.get(oUserBean, expand - 1);
            this.setObj_usuario(oUserBean);
        } else {
            this.setId_usuario(oResultSet.getInt("id_usuario"));
        }
        return this;
    }
}
