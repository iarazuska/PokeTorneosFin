package com.example.demo.DB4O;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UsuariosDb4o {

    private static final String DB_FILE = "C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Ficheros\\credenciales.db4o";

    public void nuevo(Usuario usuario) {
        ObjectContainer db = Db4oEmbedded.openFile(DB_FILE);
        try {
            db.store(usuario);
            System.out.println("usuario guardado");
        } finally {
            db.close();
        }
    }

    public void actualizar(Usuario usuarioAntiguo, Usuario usuarioNuevo) {
        ObjectContainer db = Db4oEmbedded.openFile(DB_FILE);
        try {
            ObjectSet<Usuario> resultado = db.queryByExample(usuarioAntiguo);

            if (!resultado.isEmpty()) {
                while (resultado.hasNext()) {
                    Usuario usuarioEncontrado = resultado.next();
                    db.delete(usuarioEncontrado);
                    db.store(usuarioNuevo);
                    System.out.println("usuario actualizado: " + usuarioNuevo.getNombre());
                }
            } else {
                System.out.println("usuario no encontrado");
            }
        } finally {
            db.close();
        }
    }

    public List<Usuario> buscarUsuariosPorPerfil(String perfil) {
        ObjectContainer db = Db4oEmbedded.openFile(DB_FILE);
        List<Usuario> listaUsuarios = new ArrayList<>();

        try {
            Query query = db.query();
            query.constrain(Usuario.class);
            query.descend("perfil").constrain(perfil);
            ObjectSet<Usuario> resultado = query.execute();

            while (resultado.hasNext()) {
                listaUsuarios.add(resultado.next());
            }
        } finally {
            db.close();
        }

        return listaUsuarios;
    }

    public Usuario buscarUsuarioPorNombreYContraseña(String nombre, String contraseña) {
        ObjectContainer db = Db4oEmbedded.openFile(DB_FILE);
        try {
            Query query = db.query();
            query.constrain(Usuario.class);
            query.descend("nombre").constrain(nombre);
            query.descend("contra").constrain(contraseña);
            ObjectSet<Usuario> resultado = query.execute();
            return resultado.isEmpty() ? null : resultado.next();
        } finally {
            db.close();
        }
    }

    public void borrar(String nombre, String contrasena, String perfil) {
        ObjectContainer db = Db4oEmbedded.openFile(DB_FILE);
        try {
            Query query = db.query();
            query.constrain(Usuario.class);
            query.descend("nombre").constrain(nombre);
            query.descend("contra").constrain(contrasena);
            query.descend("perfil").constrain(perfil);

            ObjectSet<Usuario> resultado = query.execute();

            if (!resultado.isEmpty()) {
                while (resultado.hasNext()) {
                    Usuario usuarioAEliminar = resultado.next();
                    db.delete(usuarioAEliminar);
                    System.out.println("Usuario eliminado: " + usuarioAEliminar.getNombre());
                }
            } else {
                System.out.println("Usuario no encontrado o credenciales incorrectas.");
            }
        } finally {
            db.close();
        }
    }
}
