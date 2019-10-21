package br.com.obt.sca.api.repository;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ActiveProfiles("test")
public class PermissaoRepositoryTest {

	// @Autowired
	// private PermissaoService permissaoService;

	// @Test
	// public void testPermissaoServiceNotNull() throws ResourceNotFoundException,
	// ResourceAlreadyRegisteredException {
	// assertThat(permissaoService).isNotNull();
	// }
	// @Test
	// public void test() throws ResourceNotFoundException,
	// ResourceAlreadyRegisteredException {
	//
	//
	//
	// assertNotNull(permissaoService);
	//
	// Permissao permissao = new Permissao();
	// permissao.setNome("nome teste");
	//
	// permissaoService.save(permissao);
	//
	// assertNotNull(permissaoService.findById(1l));
	//
	//// System.out.println("Rest : " +
	// this.restTemplate.getForObject("http://localhost:8080/", String.class));
	//// assertThat(this.restTemplate.getForObject("http://localhost:8080/",
	// String.class)).contains("Hello");
	// }

	//

	// public static final String ROLE_ALTERAR_ATRIBUTO = "ROLE_ALTERAR_ATRIBUTO";
	//
	// @Autowired
	// PermissaoRepository permissaoRepository;
	//
	// @Before
	// public void setUp() throws Exception {
	// Permissao permissao = criarPermissao(Boolean.TRUE,
	// PermissaoRepositoryTest.ROLE_ALTERAR_ATRIBUTO, "Gênero");
	// this.permissaoRepository.save(permissao);
	// }
	//
	// private Permissao criarPermissao(Boolean ativo, String nome, String
	// descricao) {
	// Permissao permissao = new Permissao();
	// permissao.setStatus(Boolean.TRUE);
	// permissao.setNome(nome);
	// permissao.setDescricao("Alterar permissao");
	// return permissao;
	// }
	//
	// @Test
	// public void testBuscarPorNome() {
	// Permissao permissao =
	// permissaoRepository.findByNome(PermissaoRepositoryTest.ROLE_ALTERAR_ATRIBUTO);
	//
	// // Deve retornar o valor igual
	// assertEquals(ROLE_ALTERAR_ATRIBUTO, permissao.getNome());
	// }
	//
	// @Test
	// public void testBuscarPorNomeNaoNulo() {
	// Permissao permissao =
	// permissaoRepository.findByNome(PermissaoRepositoryTest.ROLE_ALTERAR_ATRIBUTO);
	//
	// // Deve ser diferente de nulo
	// assertNotNull(permissao);
	// }
	//
	// @Test
	// public void testBuscarPorNomeParaAlterarRegistroModificaDataAlteracao() {
	// Permissao permissaoAntes =
	// permissaoRepository.findByNome(PermissaoRepositoryTest.ROLE_ALTERAR_ATRIBUTO);
	// Permissao permissao =
	// permissaoRepository.findByNome(PermissaoRepositoryTest.ROLE_ALTERAR_ATRIBUTO);
	//
	// permissao.setNome(PermissaoRepositoryTest.ROLE_ALTERAR_ATRIBUTO);
	//
	// permissao = permissaoRepository.save(permissao);
	//
	// // As datas de alteração devem ser diferentes
	// System.out.println("atributoAntes : " + permissaoAntes.getDataAlteracao());
	// System.out.println("atributoDepois : " + permissao.getDataAlteracao());
	// assertNotEquals(permissaoAntes.getDataAlteracao(),
	// permissao.getDataAlteracao());
	// }
	//
	// @After
	// public final void tearDown() throws ResourceNotFoundException {
	//
	// this.permissaoService.deleteById(1l);
	// }

}
