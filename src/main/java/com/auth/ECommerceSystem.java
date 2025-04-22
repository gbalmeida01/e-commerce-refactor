package com.auth;

import java.util.*;
import java.text.SimpleDateFormat;

public class ECommerceSystem {

    private List<Produto> produtos = new ArrayList();
    private List<Usuario> usuarios = new ArrayList();
    private List<Pedido> pedidos = new ArrayList();
    private static int nextPedidoId = 1;

    // Métodos para Produtos
    public void cadastrarProduto(int id, String nome, String desc, double preco, int estoque) {
        Produto p = new Produto();
        p.id = id;
        p.nome = nome;
        p.descricao = desc;
        p.preco = preco;
        p.estoque = estoque;
        produtos.add(p);
    }

    public void atualizarEstoque(int produtoId, int quantidade) {
        for (Produto p : produtos) {
            if (p.id == produtoId) {
                p.estoque += quantidade;
                return;
            }
        }
    }

    // Métodos para Usuários
    public void registrarUsuario(String email, String senha, String nome, String endereco) {
        Usuario u = new Usuario();
        u.email = email;
        u.senha = senha; // Sem hash - problema de segurança!
        u.nome = nome;
        u.endereco = endereco;
        usuarios.add(u);
    }

    // Métodos para Pedidos
    public int criarPedido(String emailUsuario) {
        Pedido p = new Pedido();
        p.id = nextPedidoId++;
        p.usuarioEmail = emailUsuario;
        p.data = new Date();
        p.itens = new HashMap<>();
        p.status = "Pendente";
        pedidos.add(p);
        return p.id;
    }

    public void adicionarItemPedido(int pedidoId, int produtoId, int quantidade) {
        for (Pedido p : pedidos) {
            if (p.id == pedidoId) {
                if (p.itens.containsKey(produtoId)) {
                    p.itens.put(produtoId, p.itens.get(produtoId) + quantidade);
                } else {
                    p.itens.put(produtoId, quantidade);
                }
                return;
            }
        }
    }

    public void finalizarPedido(int pedidoId) {
        for (Pedido p : pedidos) {
            if (p.id == pedidoId) {
                p.status = "Finalizado";
                for (Map.Entry<Integer, Integer> item : p.itens.entrySet()) {
                    for (Produto prod : produtos) {
                        if (prod.id == item.getKey()) {
                            prod.estoque -= item.getValue();
                        }
                    }
                }
                return;
            }
        }
    }

    // Métodos de relatório
    public void gerarRelatorioVendas() {
        double total = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Relatório de Vendas");
        System.out.println("===================");

        for (Pedido p : pedidos) {
            if (p.status.equals("Finalizado")) {
                double valorPedido = 0;
                System.out.println("Pedido #" + p.id + " - " + sdf.format(p.data));
                System.out.println("Itens:");

                for (Map.Entry<Integer, Integer> item : p.itens.entrySet()) {
                    Produto prod = getProdutoById(item.getKey());
                    if (prod != null) {
                        System.out.println("- " + prod.nome + ": " + item.getValue() + " x " + prod.preco);
                        valorPedido += item.getValue() * prod.preco;
                    }
                }

                System.out.println("Total do Pedido: R$" + valorPedido);
                total += valorPedido;
            }
        }

        System.out.println("===================");
        System.out.println("Total Geral: R$" + total);
    }

    // Métodos auxiliares
    private Produto getProdutoById(int id) {
        for (Produto p : produtos) {
            if (p.id == id) return p;
        }
        return null;
    }

    // Classes internas
    class Produto {
        int id;
        String nome;
        String descricao;
        double preco;
        int estoque;
    }

    class Usuario {
        String email;
        String senha;
        String nome;
        String endereco;
    }

    class Pedido {
        int id;
        String usuarioEmail;
        Date data;
        Map<Integer, Integer> itens; // produtoId -> quantidade
        String status;
    }

    public static void main(String[] args) {
        ECommerceSystem sistema = new ECommerceSystem();

        // Cadastro de produtos
        sistema.cadastrarProduto(1, "Smartphone", "Modelo X", 2500.0, 10);
        sistema.cadastrarProduto(2, "Notebook", "Modelo Y", 4500.0, 5);
        sistema.cadastrarProduto(3, "Tablet", "Modelo Z", 1200.0, 8);

        // Cadastro de usuário
        sistema.registrarUsuario("cliente@email.com", "senha123", "João Silva", "Rua A, 123");

        // Processo de compra
        int pedidoId = sistema.criarPedido("cliente@email.com");
        sistema.adicionarItemPedido(pedidoId, 1, 2); // 2 smartphones
        sistema.adicionarItemPedido(pedidoId, 3, 1); // 1 tablet
        sistema.finalizarPedido(pedidoId);

        // Gerar relatório
        sistema.gerarRelatorioVendas();
    }
}