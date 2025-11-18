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
  let pedidoEditandoId = null

  const modal = document.getElementById("modalItens")
  const selectProduto = document.getElementById("selectProduto")
  const inputQuantidade = document.getElementById("quantidade")
  const tabelaItensCorpo = modal.querySelector("tbody")

  let itensDoPedido = []

  async function buscarProdutosServicos(){
    try{
      const r = await fetch("/produtos-servicos")
      if(!r.ok) throw new Error()
      const dados = await r.json()
      populaTabelaProdutos(dados)
      populaSelectItens(dados)
    } catch(e){}
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
        if(!r.ok) throw new Error()
        produtoEditandoId = null
      } else {
        const r = await fetch("/produtos-servicos", {
          method: "POST",
          headers: {"Content-Type":"application/json"},
          body: JSON.stringify(payload)
        })
        if(!r.ok) throw new Error()
      }
      formularioProduto.reset()
      formularioProduto.classList.remove("was-validated")
      await buscarProdutosServicos()
    } catch(err){
      console.log("Falha ao salvar produto/serviço")
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
      if(!r.ok) throw new Error()
      await buscarProdutosServicos()
    } catch(e){
      console.log("Falha ao excluir produto/serviço")
    }
  }

  async function buscarPedidos(){
    try{
      const r = await fetch("/pedidos")
      if(!r.ok) throw new Error()
      const dados = await r.json()
      populaTabelaPedidos(dados)
    } catch(e){}
  }

  function populaTabelaPedidos(dados){
    tabelaPedidosCorpo.innerHTML = ""
    dados.forEach(p => {
      const tr = document.createElement("tr")
      const tdCliente = document.createElement("td")
      tdCliente.textContent = p.cliente
      const tdBase = document.createElement("td")
      tdBase.textContent = Number(p.precoBase).toFixed(2)
      const tdDesc = document.createElement("td")
      tdDesc.textContent = `${Number(p.desconto).toFixed(2)}%`
      const tdTotal = document.createElement("td")
      tdTotal.textContent = Number(p.precoFinal).toFixed(2)
      const tdAcoes = document.createElement("td")
      const btnVer = document.createElement("button")
      btnVer.className = "btn btn-sm btn-primary me-2"
      btnVer.textContent = "Ver"
      btnVer.addEventListener("click", () => verPedido(p))
      const btnEditar = document.createElement("button")
      btnEditar.className = "btn btn-sm btn-warning me-2"
      btnEditar.textContent = "Editar"
      btnEditar.addEventListener("click", () => iniciarEdicaoPedido(p))
      const btnExcluir = document.createElement("button")
      btnExcluir.className = "btn btn-sm btn-danger"
      btnExcluir.textContent = "Excluir"
      btnExcluir.addEventListener("click", () => excluirPedido(p.id))
      tdAcoes.appendChild(btnVer)
      tdAcoes.appendChild(btnEditar)
      tdAcoes.appendChild(btnExcluir)
      tr.appendChild(tdCliente)
      tr.appendChild(tdBase)
      tr.appendChild(tdDesc)
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
      const counts = p.itens.reduce((acc,id)=>{acc[id]=(acc[id]||0)+1; return acc},{})
      const uniqueIds = Object.keys(counts)
      try{
        const detalhes = await Promise.all(uniqueIds.map(async id => {
          try{
            const r = await fetch(`/produtos-servicos/${id}`)
            if(!r.ok) return null
            return await r.json()
          } catch(e){
            return null
          }
        }))
        detalhes.forEach(d => {
          if(d){
            const qtd = counts[d.id] || 1
            texto += ` - ${d.nome} — ${qtd} × R$ ${Number(d.preco).toFixed(2)} = R$ ${Number((d.preco * qtd).toFixed(2))} (${d.ehProduto ? "produto" : "serviço"})\n`
          } else {
            texto += " - (item não encontrado)\n"
          }
        })
      } catch(e){
        texto += " (erro ao recuperar detalhes)\n"
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
      if(!r.ok) throw new Error()
      await buscarPedidos()
    } catch(e){
      console.log("Falha ao excluir pedido")
    }
  }

  modal.addEventListener("show.bs.modal", function(){
    inputQuantidade.value = ""
    modal.querySelector("form").classList.remove("was-validated")
  })

  modal.querySelector("form").addEventListener("submit", async function(e){
    e.preventDefault()
    if(!modal.querySelector("form").checkValidity()){
      modal.querySelector("form").classList.add("was-validated")
      return
    }
    const idSelecionado = selectProduto.value
    if(!idSelecionado) return
    const quantidade = Math.max(1, Math.floor(Number(inputQuantidade.value) || 1))
    for(let i = 0; i < quantidade; i++){
      itensDoPedido.push(idSelecionado)
    }
    await renderizarTabelaItens()
    modal.querySelector("form").classList.remove("was-validated")
    selectProduto.value = ""
    inputQuantidade.value = ""
  })

  async function renderizarTabelaItens(){
    tabelaItensCorpo.innerHTML = ""

    const contagem = itensDoPedido.reduce((acc, id) => {
      acc[id] = (acc[id] || 0) + 1
      return acc
    }, {})

    const idsUnicos = Object.keys(contagem)

    if(idsUnicos.length === 0) return

    const detalhes = await Promise.all(
      idsUnicos.map(async id => {
        try{
          const r = await fetch(`/produtos-servicos/${id}`)
          if(!r.ok){
            return { id, nome: "(item não encontrado)", preco: 0, ehProduto: false }
          }
          return await r.json()
        } catch(e){
          return { id, nome: "(erro ao carregar)", preco: 0, ehProduto: false }
        }
      })
    )

    const itensCompletos = detalhes.map(d => {
      const qtd = contagem[d.id] || 0
      const preco = Number(d.preco) || 0
      return {
        id: d.id,
        nome: d.nome,
        preco: preco,
        ehProduto: d.ehProduto,
        quantidade: qtd,
        subtotal: Number((preco * qtd).toFixed(2))
      }
    })

    itensCompletos.forEach((item, idx) => {
      const tr = document.createElement("tr")
      const tdNome = document.createElement("td")
      tdNome.textContent = item.nome
      const tdTipo = document.createElement("td")
      tdTipo.textContent = item.ehProduto ? "Produto" : "Serviço"
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
        const id = item.id
        itensDoPedido = itensDoPedido.filter(x => x !== id)
        renderizarTabelaItens()
      })

      tdAcoes.appendChild(btnRemover)
      tr.appendChild(tdNome)
      tr.appendChild(tdTipo)
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
      return
    }

    const descontoPercentual = Number(inputDesconto.value) || 0
    const contagem = itensDoPedido.reduce((acc, id) => {
      acc[id] = (acc[id] || 0) + 1
      return acc
    }, {})

    const idsUnicos = Object.keys(contagem)

    const detalhes = await Promise.all(idsUnicos.map(async id => {
      try{
        const r = await fetch(`/produtos-servicos/${id}`)
        if(!r.ok) return null
        return await r.json()
      } catch(e){
        return null
      }
    }))

    const precoBasePedido = detalhes.reduce((total, d) => {
      if(!d) return total
      const qtd = contagem[d.id] || 0
      return total + (Number(d.preco) * qtd)
    }, 0)

    const fatorDesconto = descontoPercentual / 100
    const precoFinalPedido = precoBasePedido * (1 - fatorDesconto)

    const itensAchatados = []
    itensDoPedido.forEach(id => itensAchatados.push(id))

    const payload = {
      cliente: inputCliente.value.trim(),
      desconto: descontoPercentual,
      precoBase: Number(precoBasePedido.toFixed(2)),
      precoFinal: Number(precoFinalPedido.toFixed(2)),
      itens: itensAchatados
    }

    try{
      if(pedidoEditandoId){
        const r = await fetch(`/pedidos/${pedidoEditandoId}`, {
          method: "PATCH",
          headers: {"Content-Type":"application/json"},
          body: JSON.stringify(payload)
        })
        if(!r.ok) throw new Error()
        pedidoEditandoId = null
      } else {
        const r = await fetch("/pedidos", {
          method: "POST",
          headers: {"Content-Type":"application/json"},
          body: JSON.stringify(payload)
        })
        if(!r.ok) throw new Error()
      }

      formularioPedido.reset()
      formularioPedido.classList.remove("was-validated")
      itensDoPedido = []
      tabelaItensCorpo.innerHTML = ""
      await buscarPedidos()
    } catch(err){
      console.log("Falha ao salvar pedido")
    }
  }

  async function iniciarEdicaoPedido(p){
    pedidoEditandoId = p.id
    inputCliente.value = p.cliente
    inputDesconto.value = Number(p.desconto).toFixed(2)

    itensDoPedido = []

    try{
      if(Array.isArray(p.itens) && p.itens.length > 0){
        p.itens.forEach(id => itensDoPedido.push(id))
      }
    } catch(e){
      console.error("Erro ao iniciar edição do pedido:", e)
    }

    await renderizarTabelaItens()
    window.scrollTo({ top: formularioPedido.offsetTop, behavior: "smooth" })
  }

  formularioPedido.addEventListener("submit", function(e){
    e.preventDefault()
    salvarPedido()
  })

  buscarProdutosServicos()
  buscarPedidos()
})
