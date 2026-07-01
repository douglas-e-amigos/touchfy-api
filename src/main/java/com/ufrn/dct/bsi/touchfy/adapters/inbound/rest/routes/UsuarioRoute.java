package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest.routes;

public class UsuarioRoute {
  public static final String ROOT = "/usuarios";
  public static final String CADASTRO = "/auth/register";
  public static final String ATUALIZAR = "/{id}";
  public static final String ATUALIZAR_FOTO_DE_PERFIL = "/{id}/foto-de-perfil";
  public static final String ME = "/me";
  public static final String LOGIN = "/auth/login";
  public static final String LOGOUT = "/auth/logout";
  public static final String REFRESH_TOKEN = "/auth/refresh";
  public static final String BUSCAR = "/{id}";
  public static final String LISTAR = "/";
}
