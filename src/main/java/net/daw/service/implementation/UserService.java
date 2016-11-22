/*
 * Copyright (c) 2016 by Rafael Angel Aznar Aparici (rafaaznar at gmail dot com)
 * 
 * bauxer server: Helps you to develop easily AJAX web applications 
 *                   by copying and modifying this Java Server.
 *
 * Sources at https://github.com/rafaelaznar/bauxer
 * 
 * bauxer server is distributed under the MIT License (MIT)
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
package net.daw.service.implementation;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.daw.bean.implementation.UserBean;
import net.daw.bean.implementation.ReplyBean;
import net.daw.bean.implementation.UserBean;
import net.daw.connection.implementation.BoneConnectionPoolImpl;
import net.daw.connection.publicinterface.ConnectionInterface;
import net.daw.dao.implementation.UserDao;
import net.daw.dao.implementation.UserDao;

import net.daw.helper.statics.AppConfigurationHelper;
import static net.daw.helper.statics.AppConfigurationHelper.getSourceConnection;
import net.daw.helper.statics.FilterBeanHelper;
import net.daw.helper.statics.JsonMessage;
import net.daw.helper.statics.Log4j;
import net.daw.helper.statics.ParameterCook;
import net.daw.service.publicinterface.TableServiceInterface;
import net.daw.service.publicinterface.ViewServiceInterface;

public class UserService implements TableServiceInterface, ViewServiceInterface {

    protected HttpServletRequest oRequest = null;

    public UserService(HttpServletRequest request) {
        oRequest = request;
    }

    private Boolean checkpermission(String strMethodName) throws Exception {
        UserBean oUserBean = (UserBean) oRequest.getSession().getAttribute("userBean");
        if (oUserBean != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ReplyBean getcount() throws Exception {
        if (this.checkpermission("getcount")) {
            String data = null;
            ArrayList<FilterBeanHelper> alFilter = ParameterCook.getFilterParams(ParameterCook.prepareFilter(oRequest));
            Connection oConnection = null;
            ConnectionInterface oDataConnectionSource = null;
            try {
                oDataConnectionSource = getSourceConnection();
                oConnection = oDataConnectionSource.newConnection();
                UserDao oUserDao = new UserDao(oConnection, (UserBean) oRequest.getSession().getAttribute("userBean"));
                data = JsonMessage.getJsonExpression(200, Long.toString(oUserDao.getCount(alFilter)));
            } catch (Exception ex) {
                Log4j.errorLog(this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName(), ex);
                throw new Exception();
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oDataConnectionSource != null) {
                    oDataConnectionSource.disposeConnection();
                }
            }
            return new ReplyBean(200, data);
        } else {
            return new ReplyBean(401, JsonMessage.getJsonMsg(401, "Unauthorized"));
        }
    }

    @Override
    public ReplyBean get() throws Exception {
        if (this.checkpermission("get")) {
            int id = ParameterCook.prepareId(oRequest);
            String data = null;
            Connection oConnection = null;
            ConnectionInterface oDataConnectionSource = null;
            try {
                oDataConnectionSource = getSourceConnection();
                oConnection = oDataConnectionSource.newConnection();
                UserDao oUserDao = new UserDao(oConnection, (UserBean) oRequest.getSession().getAttribute("userBean"));
                UserBean oUserBean = new UserBean(id);
                oUserBean = oUserDao.get(oUserBean, AppConfigurationHelper.getJsonMsgDepth());
                Gson gson = AppConfigurationHelper.getGson();
                data = JsonMessage.getJsonExpression(200, AppConfigurationHelper.getGson().toJson(oUserBean));
            } catch (Exception ex) {
                Log4j.errorLog(this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName(), ex);
                throw new Exception();
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oDataConnectionSource != null) {
                    oDataConnectionSource.disposeConnection();
                }
            }
            return new ReplyBean(200, data);
        } else {
            return new ReplyBean(401, JsonMessage.getJsonMsg(401, "Unauthorized"));
        }
    }

    @Override
    public ReplyBean getall() throws Exception {
        if (this.checkpermission("getall")) {
            HashMap<String, String> hmOrder = ParameterCook.getOrderParams(ParameterCook.prepareOrder(oRequest));
            ArrayList<FilterBeanHelper> alFilter = ParameterCook.getFilterParams(ParameterCook.prepareFilter(oRequest));
            String data = null;
            Connection oConnection = null;
            ConnectionInterface oDataConnectionSource = null;
            try {
                oDataConnectionSource = getSourceConnection();
                oConnection = oDataConnectionSource.newConnection();
                UserDao oUserDao = new UserDao(oConnection, (UserBean) oRequest.getSession().getAttribute("userBean"));
                ArrayList<UserBean> arrBeans = oUserDao.getAll(alFilter, hmOrder, AppConfigurationHelper.getJsonMsgDepth());
                data = JsonMessage.getJsonExpression(200, AppConfigurationHelper.getGson().toJson(arrBeans));
            } catch (Exception ex) {
                Log4j.errorLog(this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName(), ex);
                throw new Exception();
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oDataConnectionSource != null) {
                    oDataConnectionSource.disposeConnection();
                }
            }
            return new ReplyBean(200, data);
        } else {
            return new ReplyBean(401, JsonMessage.getJsonMsg(401, "Unauthorized"));
        }
    }

    @Override
    public ReplyBean getpage() throws Exception {
        if (this.checkpermission("getpage")) {
            int intRegsPerPag = ParameterCook.prepareRpp(oRequest);
            int intPage = ParameterCook.preparePage(oRequest);
            HashMap<String, String> hmOrder = ParameterCook.getOrderParams(ParameterCook.prepareOrder(oRequest));
            ArrayList<FilterBeanHelper> alFilter = ParameterCook.getFilterParams(ParameterCook.prepareFilter(oRequest));
            String data = null;
            Connection oConnection = null;
            ConnectionInterface oDataConnectionSource = null;
            try {
                oDataConnectionSource = getSourceConnection();
                oConnection = oDataConnectionSource.newConnection();
                UserDao oUserDao = new UserDao(oConnection, (UserBean) oRequest.getSession().getAttribute("userBean"));
                List<UserBean> arrBeans = oUserDao.getPage(intRegsPerPag, intPage, alFilter, hmOrder, AppConfigurationHelper.getJsonMsgDepth());
                data = JsonMessage.getJsonExpression(200, AppConfigurationHelper.getGson().toJson(arrBeans));
            } catch (Exception ex) {
                Log4j.errorLog(this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName(), ex);
                throw new Exception();
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oDataConnectionSource != null) {
                    oDataConnectionSource.disposeConnection();
                }
            }
            return new ReplyBean(200, data);
        } else {
            return new ReplyBean(401, JsonMessage.getJsonMsg(401, "Unauthorized"));
        }
    }

    @Override
    public ReplyBean remove() throws Exception {
        if (this.checkpermission("remove")) {
            Integer id = ParameterCook.prepareId(oRequest);
            String data = null;
            Connection oConnection = null;
            ConnectionInterface oDataConnectionSource = null;
            try {
                oDataConnectionSource = getSourceConnection();
                oConnection = oDataConnectionSource.newConnection();
                oConnection.setAutoCommit(false);
                UserDao oUserDao = new UserDao(oConnection, (UserBean) oRequest.getSession().getAttribute("userBean"));
                data = JsonMessage.getJsonExpression(200, (String) oUserDao.remove(id).toString());
                oConnection.commit();
            } catch (Exception ex) {
                if (oConnection != null) {
                    oConnection.rollback();
                }
                Log4j.errorLog(this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName(), ex);
                throw new Exception();
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oDataConnectionSource != null) {
                    oDataConnectionSource.disposeConnection();
                }
            }
            return new ReplyBean(200, data);
        } else {
            return new ReplyBean(401, JsonMessage.getJsonMsg(401, "Unauthorized"));
        }
    }

    @Override
    public ReplyBean set() throws Exception {
        if (this.checkpermission("set")) {
            String jason = ParameterCook.prepareJson(oRequest);
            ReplyBean oReplyBean = new ReplyBean();
            Connection oConnection = null;
            ConnectionInterface oDataConnectionSource = null;
            try {
                oDataConnectionSource = getSourceConnection();
                oConnection = oDataConnectionSource.newConnection();
                oConnection.setAutoCommit(false);
                UserDao oUserDao = new UserDao(oConnection, (UserBean) oRequest.getSession().getAttribute("userBean"));
                UserBean oUserBean = new UserBean();
                oUserBean = AppConfigurationHelper.getGson().fromJson(jason, oUserBean.getClass());
                if (oUserBean != null) {
                    Integer iResult = oUserDao.set(oUserBean);
                    if (iResult >= 1) {
                        oReplyBean.setCode(200);
                        oReplyBean.setJson(JsonMessage.getJsonExpression(200, iResult.toString()));
                    } else {
                        oReplyBean.setCode(500);
                        oReplyBean.setJson(JsonMessage.getJsonMsg(500, "Error during registry set"));
                    }
                } else {
                    oReplyBean.setCode(500);
                    oReplyBean.setJson(JsonMessage.getJsonMsg(500, "Error during registry set"));
                }
                oConnection.commit();
            } catch (Exception ex) {
                if (oConnection != null) {
                    oConnection.rollback();
                }
                Log4j.errorLog(this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName(), ex);
                throw new Exception();
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oDataConnectionSource != null) {
                    oDataConnectionSource.disposeConnection();
                }
            }
            return oReplyBean;
        } else {
            return new ReplyBean(401, JsonMessage.getJsonMsg(401, "Unauthorized"));
        }
    }

    public ReplyBean login() throws SQLException, Exception {
        UserBean oUserBean = (UserBean) oRequest.getSession().getAttribute("userBean");
        String strAnswer = null;
        String strCode = "200";
        try {
            if (oUserBean == null) {
                String login = oRequest.getParameter("user");
                String pass = oRequest.getParameter("pass");
                if (!login.equals("") && !pass.equals("")) {
                    ConnectionInterface oDataConnectionSource = null;
                    Connection oConnection = null;
                    try {
                        oDataConnectionSource = getSourceConnection();
                        oConnection = oDataConnectionSource.newConnection();
                        UserBean oUser = new UserBean();
                        oUser.setLogin(login);
                        oUser.setPassword(pass);
                        UserDao oUserDao = new UserDao(oConnection, (UserBean) oRequest.getSession().getAttribute("userBean"));
                        oUser = oUserDao.getFromLogin(oUser);
                        if (oUser.getId() != 0) {
                            oRequest.getSession().setAttribute("userBean", oUser);
                            strAnswer = oUser.getLogin();
                        } else {
                            strCode = "403";
                            strAnswer = "User or password incorrect";
                        }
                    } catch (Exception ex) {
                        Log4j.errorLog(this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName(), ex);
                        strCode = "403";
                        strAnswer = "ERROR01";
                    } finally {
                        if (oConnection != null) {
                            oConnection.close();
                        }
                        if (oDataConnectionSource != null) {
                            oDataConnectionSource.disposeConnection();
                        }
                    }
                }
            } else {
                strAnswer = "Already logged in";
            }
        } catch (Exception ex) {
            Log4j.errorLog(this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName(), ex);
            strCode = "403";
            strAnswer = "ERROR02";
        }
        return new ReplyBean(Integer.parseInt(strCode), JsonMessage.getJsonMsg(Integer.parseInt(strCode), strAnswer));

    }

    public ReplyBean logout() {
        oRequest.getSession().invalidate();
        return new ReplyBean(200, JsonMessage.getJsonMsg(200, "bye"));
    }

    public ReplyBean getsessionstatus() {
        String strAnswer = null;
        UserBean oUserBean = (UserBean) oRequest.getSession().getAttribute("userBean");
        if (oUserBean == null) {
            return new ReplyBean(403, JsonMessage.getJsonMsg(403, "Unauthorized"));
        } else {
            return new ReplyBean(200, JsonMessage.getJsonMsg(200, oUserBean.getLogin()));
        }
    }

    public ReplyBean sessionuserlevel() {
        String strAnswer = null;
        UserBean oUserBean = (UserBean) oRequest.getSession().getAttribute("userBean");
        Map<Integer, String> map = new HashMap<>();
        if (oUserBean == null) {
            return new ReplyBean(403, JsonMessage.getJsonMsg(403, "Unauthorized"));
        } else {
            return new ReplyBean(200, JsonMessage.getJsonExpression(200, oUserBean.getId_usertype().toString()));
        }
    }

}
