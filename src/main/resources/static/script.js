document.addEventListener("DOMContentLoaded", function(){
  const formularioProduto = document.querySelectorAll(".card form")[0]
  const inputNomeProduto = document.getElementById("nomeProduto")
  const inputPrecoProduto = document.getElementById("precoProduto")
  const inputIsServico = document.getElementById("isServico")
  const tabelaProdutosCorpo = document.querySelectorAll(".card")[1].querySelector("tbody")
  let produtoEditandoId = null

  const formularioPedido = document.querySelectorAll(".card form")[1]
  const inputCliente = document.getElementById("cliente")
  const inputDesconto = document.getElementById("desconto")
  const tabelaPedidosCorpo = document.querySelectorAll(".card")[2].querySelector("tbody")
  const botaoAbrirModalItens = document.querySelector('[data-bs-target="#modalItens"]')
  let pedidoEditandoId = null

  const modal = document.getElementById("modalItens")
  const selectProduto = document.getElementById("selectProduto")
  const inputQuantidade = document.getElementById("quantidade")
  const tabelaItensCorpo = modal.querySelector("tbody")
  let itensDoPedido = []

  async function buscarProdutosServicos(){
    try{
      const r = await fetch("/produtos-servicos")
      if(!r.ok) throw new Error("Erro ao buscar produtos")
      const dados = await r.json()
      populaTabelaProdutos(dados)
      populaSelectItens(dados)
    } catch(e){
      console.error(e)
    }
  }

  function populaTabelaProdutos(dados){
    tabelaProdutosCorpo.innerHTML = ""
    dados.forEach(p => {
      const tr = document.createElement("tr")

      const tdNome = document.createElement("td")
      tdNome.textContent = p.nome

      const tdTipo = document.createElement("td")
      const badge = document.createElement("span")
      badge.className = "badge " + (p.ehProduto ? "bg-primary" : "bg-secondary")
      badge.textContent = p.ehProduto ? "Produto" : "Serviço"
      tdTipo.appendChild(badge)

      const tdPreco = document.createElement("td")
      tdPreco.textContent = Number(p.preco).toFixed(2)

      const tdAcoes = document.createElement("td")
      const btnEditar = document.createElement("button")
      btnEditar.className = "btn btn-sm btn-warning me-2"
      btnEditar.textContent = "Editar"
      btnEditar.addEventListener("click", () => iniciarEdicaoProduto(p))

      const btnExcluir = document.createElement("button")
      btnExcluir.className = "btn btn-sm btn-danger"
      btnExcluir.textContent = "Excluir"
      btnExcluir.addEventListener("click", () => excluirProduto(p.id))

      tdAcoes.appendChild(btnEditar)
      tdAcoes.appendChild(btnExcluir)

      tr.appendChild(tdNome)
      tr.appendChild(tdTipo)
      tr.appendChild(tdPreco)
      tr.appendChild(tdAcoes)

      tabelaProdutosCorpo.appendChild(tr)
    })
  }

  function populaSelectItens(dados){
    selectProduto.innerHTML = '<option value="">Selecione...</option>'
    dados.forEach(i => {
      const opt = document.createElement("option")
      opt.value = i.id
      opt.textContent = `${i.nome} (${i.ehProduto ? "produto" : "serviço"}) - ${Number(i.preco).toFixed(2)}`
      opt.dataset.preco = i.preco
      opt.dataset.nome = i.nome
      opt.dataset.ehproduto = i.ehProduto
      selectProduto.appendChild(opt)
    })
  }

  formularioProduto.addEventListener("submit", async function(e){
    e.preventDefault()

    if(!formularioProduto.checkValidity()){
      formularioProduto.classList.add("was-validated")
      return
    }

    const payload = {
      nome: inputNomeProduto.value.trim(),
      preco: Number(inputPrecoProduto.value),
      ehProduto: !inputIsServico.checked
    }

    try{
      if(produtoEditandoId){
        const r = await fetch(`/produtos-servicos/${produtoEditandoId}`, {
          method: "PATCH",
          headers: {"Content-Type":"application/json"},
          body: JSON.stringify(payload)
        })
        if(!r.ok) throw new Error("Erro ao atualizar")
        produtoEditandoId = null
      } else {
        const r = await fetch("/produtos-servicos", {
          method: "POST",
          headers: {"Content-Type":"application/json"},
          body: JSON.stringify(payload)
        })
        if(!r.ok) throw new Error("Erro ao criar")
      }

      formularioProduto.reset()
      formularioProduto.classList.remove("was-validated")
      await buscarProdutosServicos()

    } catch(err){
      alert("Falha ao salvar produto/serviço")
      console.error(err)
    }
  })

  function iniciarEdicaoProduto(p){
    produtoEditandoId = p.id
    inputNomeProduto.value = p.nome
    inputPrecoProduto.value = Number(p.preco).toFixed(2)
    inputIsServico.checked = !p.ehProduto
    window.scrollTo({top:0, behavior:"smooth"})
  }

  async function excluirProduto(id){
    if(!confirm("Confirma exclusão?")) return

    try{
      const r = await fetch(`/produtos-servicos/${id}`, {method:"DELETE"})
      if(!r.ok) throw new Error("Erro ao excluir")
      await buscarProdutosServicos()
    } catch(e){
      alert("Falha ao excluir produto/serviço")
      console.error(e)
    }
  }

  async function buscarPedidos(){
    try{
      const r = await fetch("/pedidos")
      if(!r.ok) throw new Error("Erro ao buscar pedidos")
      const dados = await r.json()
      populaTabelaPedidos(dados)
    } catch(e){
      console.error(e)
    }
  }

  function populaTabelaPedidos(dados){
    tabelaPedidosCorpo.innerHTML = ""

    dados.forEach(p => {
      const tr = document.createElement("tr")

      const tdCliente = document.createElement("td")
      tdCliente.textContent = p.cliente

      const tdItens = document.createElement("td")
      tdItens.textContent = Array.isArray(p.itens) ? p.itens.length : 0

      const tdDesconto = document.createElement("td")
      tdDesconto.textContent = Number(p.desconto).toFixed(2)

      const tdTotal = document.createElement("td")
      tdTotal.textContent = Number(p.precoFinal).toFixed(2)

      const tdAcoes = document.createElement("td")
      const btnVer = document.createElement("button")
      btnVer.className = "btn btn-sm btn-primary me-2"
      btnVer.textContent = "Ver"
      btnVer.addEventListener("click", () => verPedido(p))

      const btnExcluir = document.createElement("button")
      btnExcluir.className = "btn btn-sm btn-danger"
      btnExcluir.textContent = "Excluir"
      btnExcluir.addEventListener("click", () => excluirPedido(p.id))

      tdAcoes.appendChild(btnVer)
      tdAcoes.appendChild(btnExcluir)

      tr.appendChild(tdCliente)
      tr.appendChild(tdItens)
      tr.appendChild(tdDesconto)
      tr.appendChild(tdTotal)
      tr.appendChild(tdAcoes)

      tabelaPedidosCorpo.appendChild(tr)
    })
  }

  async function verPedido(p){
    let texto =
      `Cliente: ${p.cliente}\n` +
      `Desconto: ${Number(p.desconto).toFixed(2)}%\n` +
      `Preço base: ${Number(p.precoBase).toFixed(2)}\n` +
      `Preço final: ${Number(p.precoFinal).toFixed(2)}\n` +
      `Itens:\n`

    if(Array.isArray(p.itens) && p.itens.length){
      try{
        const detalhes = await Promise.all(p.itens.map(async (uuid) => {
          try{
            const r = await fetch(`/produtos-servicos/${uuid}`)
            if(!r.ok) return null
            return await r.json()
          } catch(e){
            return null
          }
        }))

        detalhes.forEach(d => {
          if(d){
            texto += ` - ${d.nome} — R$ ${Number(d.preco).toFixed(2)} (${d.ehProduto ? "produto" : "serviço"})\n`
          } else {
            texto += " - (item não encontrado)\n"
          }
        })
      } catch(e){
        console.error(e)
        texto += " (erro ao recuperar detalhes dos itens)\n"
      }
    } else {
      texto += " (sem itens)\n"
    }

    alert(texto)
  }

  async function excluirPedido(id){
    if(!confirm("Confirma exclusão do pedido?")) return

    try{
      const r = await fetch(`/pedidos/${id}`, {method:"DELETE"})
      if(!r.ok) throw new Error("Erro ao excluir pedido")
      await buscarPedidos()
    } catch(e){
      alert("Falha ao excluir pedido")
      console.error(e)
    }
  }

  modal.addEventListener("show.bs.modal", function(){
    itensDoPedido = []
    tabelaItensCorpo.innerHTML = ""
    inputQuantidade.value = ""
  })

  modal.querySelector("form").addEventListener("submit", function(e){
    e.preventDefault()

    if(!modal.querySelector("form").checkValidity()){
      modal.querySelector("form").classList.add("was-validated")
      return
    }

    const idSelecionado = selectProduto.value
    if(!idSelecionado) return

    const opcao = selectProduto.querySelector(`option[value="${idSelecionado}"]`)
    const nome = opcao.dataset.nome || opcao.textContent
    const preco = Number(opcao.dataset.preco || 0)
    const ehProduto = (opcao.dataset.ehproduto === "true")

    const quantidade = Math.max(1, Math.floor(Number(inputQuantidade.value) || 1))

    const existenteIndex = itensDoPedido.findIndex(x => x.id === idSelecionado)

    if(existenteIndex > -1){
      itensDoPedido[existenteIndex].quantidade += quantidade
      itensDoPedido[existenteIndex].subtotal =
        Number((itensDoPedido[existenteIndex].quantidade * preco).toFixed(2))
    } else {
      itensDoPedido.push({
        id: idSelecionado,
        nome,
        preco,
        ehProduto,
        quantidade,
        subtotal: Number((quantidade * preco).toFixed(2))
      })
    }

    renderizarTabelaItens()

    modal.querySelector("form").classList.remove("was-validated")
    selectProduto.value = ""
    inputQuantidade.value = ""
  })

  function renderizarTabelaItens(){
    tabelaItensCorpo.innerHTML = ""

    itensDoPedido.forEach((item, idx) => {
      const tr = document.createElement("tr")

      const tdNome = document.createElement("td")
      tdNome.textContent = item.nome

      const tdQtd = document.createElement("td")
      tdQtd.textContent = item.quantidade

      const tdPreco = document.createElement("td")
      tdPreco.textContent = item.preco.toFixed(2)

      const tdSubtotal = document.createElement("td")
      tdSubtotal.textContent = item.subtotal.toFixed(2)

      const tdAcoes = document.createElement("td")
      const btnRemover = document.createElement("button")
      btnRemover.className = "btn btn-sm btn-danger"
      btnRemover.textContent = "Remover"
      btnRemover.addEventListener("click", () => {
        itensDoPedido.splice(idx, 1)
        renderizarTabelaItens()
      })
      tdAcoes.appendChild(btnRemover)

      tr.appendChild(tdNome)
      tr.appendChild(tdQtd)
      tr.appendChild(tdPreco)
      tr.appendChild(tdSubtotal)
      tr.appendChild(tdAcoes)

      tabelaItensCorpo.appendChild(tr)
    })
  }

  async function salvarPedido(){
    if(!formularioPedido.checkValidity()){
      formularioPedido.classList.add("was-validated")
      return
    }

    if(itensDoPedido.length === 0){
      alert("Inclua ao menos um item no pedido.")
      return
    }

    const payload = {
      cliente: inputCliente.value.trim(),
      desconto: Number(inputDesconto.value),
      itens: itensDoPedido.map(i => i.id)
    }

    try{
      if(pedidoEditandoId){
        const r = await fetch(`/pedidos/${pedidoEditandoId}`, {
          method: "PATCH",
          headers: {"Content-Type":"application/json"},
          body: JSON.stringify(payload)
        })
        if(!r.ok) throw new Error("Erro ao atualizar pedido")
        pedidoEditandoId = null
      } else {
        const r = await fetch("/pedidos", {
          method: "POST",
          headers: {"Content-Type":"application/json"},
          body: JSON.stringify(payload)
        })
        if(!r.ok) throw new Error("Erro ao criar pedido")
      }

      formularioPedido.reset()
      formularioPedido.classList.remove("was-validated")
      itensDoPedido = []
      tabelaItensCorpo.innerHTML = ""

      await buscarPedidos()

    } catch(err){
      alert("Falha ao salvar pedido")
      console.error(err)
    }
  }

  async function iniciarEdicaoPedido(p){
    pedidoEditandoId = p.id
    inputCliente.value = p.cliente
    inputDesconto.value = Number(p.desconto).toFixed(2)

    itensDoPedido = []

    if(Array.isArray(p.itens) && p.itens.length){
      try{
        const detalhes = await Promise.all(p.itens.map(async (uuid) => {
          try{
            const r = await fetch(`/produtos-servicos/${uuid}`)
            if(!r.ok) return null
            return await r.json()
          } catch(e){
            return null
          }
        }))

        detalhes.forEach(d => {
          if(d){
            itensDoPedido.push({
              id: d.id,
              nome: d.nome,
              preco: Number(d.preco),
              quantidade: 1,
              subtotal: Number(d.preco),
              ehProduto: d.ehProduto
            })
          } else {
            itensDoPedido.push({
              id: null,
              nome: "(item não encontrado)",
              preco: 0,
              quantidade: 1,
              subtotal: 0,
              ehProduto: false
            })
          }
        })
      } catch(e){
        console.error(e)
      }
    }

    renderizarTabelaItens()
    window.scrollTo({top: formularioPedido.offsetTop, behavior:"smooth"})
  }

  formularioPedido.addEventListener("submit", function(e){
    e.preventDefault()
    salvarPedido()
  })

  buscarProdutosServicos()
  buscarPedidos()
})
