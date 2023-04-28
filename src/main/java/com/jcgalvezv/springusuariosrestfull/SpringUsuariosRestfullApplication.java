package com.jcgalvezv.springusuariosrestfull;

import com.jcgalvezv.springusuariosrestfull.config.RsaJwtKeyProperties;
import com.jcgalvezv.springusuariosrestfull.dao.UsuarioDao;
import com.jcgalvezv.springusuariosrestfull.entities.Telefono;
import com.jcgalvezv.springusuariosrestfull.entities.Usuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
@EnableConfigurationProperties(RsaJwtKeyProperties.class)
public class SpringUsuariosRestfullApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringUsuariosRestfullApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(UsuarioDao usuarioDao) {
//		return args -> {
//			Usuario usuario = new Usuario(
//					"Juan Carlos Galvez Villegas",
//					"jcgalvezv@localhost.cl",
//					"P@ssw0rd",
//					new ArrayList<Telefono>() {{
//						add(new Telefono("12345679", "11", "57"));
//						add(new Telefono("87654321", "22", "503"));
//					}});
//			try {
//				usuarioDao.save(usuario);
//			}
//			catch (Exception e) {
//				System.out.println(e);
//			}
//		};
//	}
}
