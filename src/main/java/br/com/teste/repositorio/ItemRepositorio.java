package br.com.teste.repositorio;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.teste.modelo.Item;

/**
 * Repositório responsável pelo acesso e manipulação dos dados de item no banco.
 */
@Repository
public interface ItemRepositorio extends JpaRepository<Item, UUID> {

}
