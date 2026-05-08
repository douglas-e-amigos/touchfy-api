package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest.routes;

public class UsuarioRoute {
    public final static String ROOT = "/usuarios";
    public final static String CADASTRO = "/auth/register";
    public final static String ATUALIZAR = "/{id}";
    public final static String ATUALIZAR_FOTO_DE_PERFIL = "/{id}/foto-de-perfil";
    public final static String LOGIN = "/auth/login";
    public final static String LOGOUT = "/auth/logout";
    public final static String REFRESH_TOKEN = "/auth/refresh";
}
