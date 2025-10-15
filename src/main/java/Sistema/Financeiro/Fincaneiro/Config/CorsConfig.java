// package Sistema.Financeiro.Fincaneiro.Config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// import java.util.Arrays;

// @Configuration
// public class CorsConfig {

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration config = new CorsConfiguration();

//         // Frontend autorizado
//         config.setAllowedOrigins(Arrays.asList("https://fintrack-finance.vercel.app"));
        
//         // Permitir qualquer header
//         config.addAllowedHeader("*");
        
//         // Permitir qualquer método
//         config.addAllowedMethod("*");
        
//         // Se for usar credenciais (cookies/autenticação)
//         config.setAllowCredentials(true);

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", config);

//         return source;
//     }
// }
