document.addEventListener("DOMContentLoaded", function(){
  const formularioProduto = document.querySelectorAll(".card form")[0]
  const inputNomeProduto = document.getElementById("nomeProduto")
  const inputPrecoProduto = document.getElementById("precoProduto")
  const inputIsServico = document.getElementById("isServico")
  const inputIsInativo = document.getElementById("isInativo")
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
      const badgeTipo = document.createElement("span")
      badgeTipo.className = "badge " + (p.ehProduto ? "bg-primary" : "bg-secondary")
      badgeTipo.textContent = p.ehProduto ? "Produto" : "Serviço"
      tdTipo.appendChild(badgeTipo)

      const tdPreco = document.createElement("td")
      tdPreco.textContent = p.preco.toFixed(2)

      const tdSituacao = document.createElement("td")
      const badgeSituacao = document.createElement("span")
      badgeSituacao.className = "badge " + (p.estahDesativado ? "bg-danger" : "bg-success")
      badgeSituacao.textContent = p.estahDesativado ? "Inativo" : "Ativo"
      tdSituacao.appendChild(badgeSituacao)

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
      tr.appendChild(tdSituacao)
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
      opt.dataset.nome = i.nome
      opt.dataset.preco = i.preco
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
      ehProduto: !inputIsServico.checked,
      estahDesativado: inputIsInativo.checked
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
    } catch(err){}
  })

  function iniciarEdicaoProduto(p){
    produtoEditandoId = p.id
    inputNomeProduto.value = p.nome
    inputPrecoProduto.value = Number(p.preco).toFixed(2)
    inputIsServico.checked = !p.ehProduto
    inputIsInativo.checked = p.estahDesativado
    window.scrollTo({top:0, behavior:"smooth"})
  }

  async function excluirProduto(id){
    if(!confirm("Confirma exclusão?")) return
    try{
      const r = await fetch(`/produtos-servicos/${id}`, {method:"DELETE"})
      if(!r.ok) throw new Error()
      await buscarProdutosServicos()
    } catch(e){}
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
      btnVer.textContent = "Ver detalhes"
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
    const verCliente = document.getElementById("verCliente")
    const verDesconto = document.getElementById("verDesconto")
    const verBase = document.getElementById("verBase")
    const verFinal = document.getElementById("verFinal")
    const verItensCorpo = document.getElementById("verItensCorpo")

    verCliente.textContent = p.cliente
    verDesconto.textContent = Number(p.desconto).toFixed(2)
    verBase.textContent = Number(p.precoBase).toFixed(2)
    verFinal.textContent = Number(p.precoFinal).toFixed(2)

    verItensCorpo.innerHTML = ""

    if(Array.isArray(p.itens) && p.itens.length){
      const counts = p.itens.reduce((acc,id)=>{
        acc[id] = (acc[id]||0) + 1
        return acc
      },{})

      const ids = Object.keys(counts)

      try{
        const detalhes = await Promise.all(
          ids.map(async id => {
            try{
              const r = await fetch(`/itens/${id}`)
              if(!r.ok) return null
              return await r.json()
            } catch(e){
              return null
            }
          })
        )

        detalhes.forEach(d => {
          const tr = document.createElement("tr")

          if(d){
            const qtd = counts[d.id]
            const subtotal = (d.preco * qtd).toFixed(2)

            const tdNome = document.createElement("td")
            tdNome.textContent = d.nome

            const tdTipo = document.createElement("td")
            tdTipo.textContent = d.ehProduto ? "Produto" : "Serviço"

            const tdPreco = document.createElement("td")
            tdPreco.textContent = Number(d.preco).toFixed(2)

            const tdQtd = document.createElement("td")
            tdQtd.textContent = qtd

            const tdSubtotal = document.createElement("td")
            tdSubtotal.textContent = subtotal

            tr.appendChild(tdNome)
            tr.appendChild(tdTipo)
            tr.appendChild(tdSituacao)
            tr.appendChild(tdPreco)
            tr.appendChild(tdQtd)
            tr.appendChild(tdSubtotal)
          } else {
            const td = document.createElement("td")
            td.colSpan = 5
            td.textContent = "(item não encontrado)"
            tr.appendChild(td)
          }

          verItensCorpo.appendChild(tr)
        })
      } catch(e){}
    } else {
      const tr = document.createElement("tr")
      const td = document.createElement("td")
      td.colSpan = 5
      td.textContent = "(sem itens)"
      tr.appendChild(td)
      verItensCorpo.appendChild(tr)
    }

    const modal = new bootstrap.Modal(document.getElementById("modalVerPedido"))
    modal.show()
  }

  async function excluirPedido(id){
    if(!confirm("Confirma exclusão do pedido?")) return
    try{
      const r = await fetch(`/pedidos/${id}`, {method:"DELETE"})
      if(!r.ok) throw new Error()
      await buscarPedidos()
    } catch(e){}
  }

  modal.addEventListener("show.bs.modal", function(){
    inputQuantidade.value = ""
    modal.querySelector("form").classList.remove("was-validated")
  })

  modal.querySelector("form").addEventListener("submit", async function(e){
    e.preventDefault()
    const form = modal.querySelector("form")
    if(!form.checkValidity()){
      form.classList.add("was-validated")
      return
    }

    const idProd = selectProduto.value
    if(!idProd) return

    const opt = selectProduto.selectedOptions[0]
    const nome = opt.dataset.nome
    const preco = Number(opt.dataset.preco)
    const ehProduto = opt.dataset.ehproduto === "true"
    const quantidade = Math.max(1, Math.floor(Number(inputQuantidade.value) || 1))

    if(indiceItemEditando === null){
      itensDoPedido.push({
        id: null,
        nome: nome,
        ehProduto: ehProduto,
        preco: preco,
        quantidade: quantidade
      })
    } else {
      const item = itensDoPedido[indiceItemEditando]
      item.nome = nome
      item.ehProduto = ehProduto
      item.preco = preco
      item.quantidade = quantidade

      if (item.id) {
        item.id = null
      }

      indiceItemEditando = null
    }

    await renderizarTabelaItens()

    selectProduto.value = ""
    inputQuantidade.value = ""
    form.classList.remove("was-validated")

    const modalBootstrap = bootstrap.Modal.getInstance(modal)
    modalBootstrap.hide()
  })

  async function renderizarTabelaItens(){
    tabelaItensCorpo.innerHTML = ""

    itensDoPedido.forEach((item, idx) => {
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
      tdSubtotal.textContent = (item.preco * item.quantidade).toFixed(2)

      const tdAcoes = document.createElement("td")

      const btnEditar = document.createElement("button")
      btnEditar.className = "btn btn-sm btn-warning me-2"
      btnEditar.textContent = "Editar"
      btnEditar.addEventListener("click", () => {
        iniciarEdicaoItem(idx)
      })

      const btnRemover = document.createElement("button")
      btnRemover.className = "btn btn-sm btn-danger"
      btnRemover.textContent = "Excluir"
      btnRemover.addEventListener("click", () => {
        itensDoPedido.splice(idx, 1)
        renderizarTabelaItens()
      })

      tdAcoes.appendChild(btnEditar)
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
    if(itensDoPedido.length === 0) return

    const descontoPercentual = Number(inputDesconto.value) || 0
    const precoBase = itensDoPedido.reduce((t, i) => t + (i.preco * i.quantidade), 0)
    const precoFinal = precoBase * (1 - descontoPercentual / 100)

    const idsAchatados = []
    for(const item of itensDoPedido){
      if(!item.id){
        const r = await fetch("/itens", {
          method: "POST",
          headers: {"Content-Type":"application/json"},
          body: JSON.stringify({
            nome: item.nome,
            ehProduto: item.ehProduto,
            preco: item.preco,
            quantidade: item.quantidade
          })
        })
        if(!r.ok) throw new Error()
        const salvo = await r.json()
        item.id = salvo.id
      }

      for(let i = 0; i < item.quantidade; i++){
        idsAchatados.push(item.id)
      }
    }

    const payload = {
      cliente: inputCliente.value.trim(),
      desconto: descontoPercentual,
      precoBase: Number(precoBase.toFixed(2)),
      precoFinal: Number(precoFinal.toFixed(2)),
      itens: idsAchatados
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
    } catch(err){}
  }

  async function iniciarEdicaoPedido(p){
    pedidoEditandoId = p.id
    inputCliente.value = p.cliente
    inputDesconto.value = Number(p.desconto).toFixed(2)
    itensDoPedido = []

    try{
      if(Array.isArray(p.itens) && p.itens.length){
        const counts = p.itens.reduce((a,id)=>{a[id]=(a[id]||0)+1; return a},{})
        const ids = Object.keys(counts)

        const detalhes = await Promise.all(
          ids.map(async id => {
            try{
              const r = await fetch(`/itens/${id}`)
              if(!r.ok) return null
              return await r.json()
            } catch(e){
              return null
            }
          })
        )

        detalhes.forEach(d => {
          if(d){
            itensDoPedido.push({
              id: d.id,
              nome: d.nome,
              ehProduto: d.ehProduto,
              preco: d.preco,
              quantidade: counts[d.id]
            })
          }
        })
      }
    } catch(e){}

    await renderizarTabelaItens()

    window.scrollTo({
      top: formularioPedido.offsetTop,
      behavior: "smooth"
    })
  }
  let indiceItemEditando = null

  function iniciarEdicaoItem(indice){
    const item = itensDoPedido[indice]
    indiceItemEditando = indice

    for(const opt of selectProduto.options){
      if(opt.dataset.nome === item.nome && Number(opt.dataset.preco) === Number(item.preco)){
        selectProduto.value = opt.value
        break
      }
    }

    inputQuantidade.value = item.quantidade

    const modalBootstrap = bootstrap.Modal.getOrCreateInstance(modal)
    modalBootstrap.show()
  }

  formularioPedido.addEventListener("submit", function(e){
    e.preventDefault()
    salvarPedido()
  })

  buscarProdutosServicos()
  buscarPedidos()
})
