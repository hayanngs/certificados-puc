package br.com.hayann;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Evento {

	public Integer id;

	private String titulo;

	private String linkCertificado;

	public Evento(Integer id, String titulo) {
		this.id = id;
		this.titulo = titulo;
	}

	public Evento(Integer id, String titulo, String linkCertificado) {
		this.id = id;
		this.titulo = titulo;
		this.linkCertificado = linkCertificado;
	}

	@Override
	public String toString() {
		return "Evento{" +
				"id=" + id +
				", titulo='" + titulo + '\'' +
				", linkCertificado='" + linkCertificado + '}';
	}
}
