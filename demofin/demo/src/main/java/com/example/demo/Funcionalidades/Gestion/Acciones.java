package com.example.demo.Funcionalidades.Gestion;

import com.example.demo.DB4O.Usuario;
import com.example.demo.DB4O.UsuariosDb4o;

import java.util.List;
import java.util.Scanner;

public class Acciones {

    public void listarAdminEntre() {
        UsuariosDb4o usuariosDb4o = new UsuariosDb4o();

        List<Usuario> usuariosAdmin = usuariosDb4o.buscarUsuariosPorPerfil("torneo");
        List<Usuario> usuariosEntre = usuariosDb4o.buscarUsuariosPorPerfil("entrenador");

        System.out.println("lista de los admins de torneo");
        for (int i = 0; i < usuariosAdmin.size(); i++) {
            System.out.println("nombre " + usuariosAdmin.get(i).getNombre() +
                    " - contra " + usuariosAdmin.get(i).getContra());
        }
        System.out.println("lista de los entrenadores");
        for (int i = 0; i < usuariosEntre.size(); i++) {
            System.out.println("nombre " + usuariosEntre.get(i).getNombre() +
                    " - contra " + usuariosEntre.get(i).getContra());
        }
    }

    public void eliminarUsu() {
        Scanner sc = new Scanner(System.in);
        UsuariosDb4o usuariosDb4o = new UsuariosDb4o();

        System.out.println("que usuario qieres borrar");
        String nombreUsu = sc.nextLine();

        System.out.println("cual es la contra");
        String contraUsu = sc.nextLine();

        System.out.println("que perfil tiene");
        String perfil = sc.nextLine();

        if (perfil.equalsIgnoreCase("torneo") || perfil.equalsIgnoreCase("AdminTorneo")) {
            perfil = "torneo";
            usuariosDb4o.borrar(nombreUsu, contraUsu, perfil);
        } else if (perfil.equalsIgnoreCase("entrenador") || perfil.equalsIgnoreCase("Entrenador")) {
            perfil = "entrenador";
            usuariosDb4o.borrar(nombreUsu, contraUsu, perfil);
        } else {
            System.out.println("no existe");
        }
    }

    public void modificarContra() {
        Scanner sc = new Scanner(System.in);
        UsuariosDb4o usuariosDb4o = new UsuariosDb4o();

        System.out.println("dime el nombre");
        String nombreUsu = sc.nextLine();

        System.out.println("cual es la contra");
        String contraUsu = sc.nextLine();

        Usuario usuario = usuariosDb4o.buscarUsuarioPorNombreYContraseña(nombreUsu, contraUsu);
        if (usuario != null) {
            System.out.println("cual es la contra nueva de" + usuario.getNombre() + "?");
            String contraNueva = sc.nextLine();

            Usuario usuarioNuevo = new Usuario(usuario.getNombre(), contraNueva, usuario.getPerfil(), usuario.getId());
            usuariosDb4o.actualizar(usuario, usuarioNuevo);
        } else {
            System.out.println("no exixte");
        }
    }
}
