package exercise.article;

import exercise.worker.Worker;
import exercise.worker.WorkerImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class WorkerImplTest {

    private Worker worker;
    List<Article> newArticlesList;

    @Mock
    private Library library;
    @DisplayName("Имя тестируемого метода")
    @BeforeEach
    public void prepare () throws Exception {
        MockitoAnnotations.openMocks(this);
        worker = new WorkerImpl(library);
        newArticlesList = new ArrayList<>();

    }
    @DisplayName("Уникальность статьи по её названию")
    @Test
    public void sameArticlesDefence() throws Exception {
        List<Article> sameArticles = new ArrayList<>();
        sameArticles.add(new Article(
                    "Same title",
                 "Same content",
                  "Same author",
                null));
        sameArticles.add(new Article(
                    "Same title",
                 "Same content",
                  "Same author",
                null));
        List<Article> newSameArticles = worker.prepareArticles(sameArticles);
        assertEquals(1,newSameArticles.size());
    }
    @DisplayName("Сохранение статей в библиотеке")
    @Test
    public void addNewArticle () {
        newArticlesList.add(new Article("MadeNewArticleTitle",
                                     "ContentContent",
                                      "AuthorAuthor",
                       LocalDate.of(    2023,1,1)));
        worker.addNewArticles(newArticlesList);
        verify(library).store(2023, newArticlesList);
    }
    @DisplayName("Формирование списка названий")
    @Test
    public void getTitlesFromCatalog () {
        when(library.getAllTitles()).thenReturn(List.of("ArticleTitle1",
                                                        "ArticleTitle2",
                                                        "ArticleTitle3"));
        assertTrue(worker.getCatalog().contains("ArticleTitle1"));
    }
    @DisplayName("Не заполнено поле \"title\"")
    @Test
    public void noTitle() {
        newArticlesList.add(new Article(null,
                                     "ContentContent",
                                      "AuthorAuthor",
                           LocalDate.of(2023,1,1)));
        List<Article> article = worker.prepareArticles(newArticlesList);
        assertEquals(0, article.size());
    }
    @DisplayName("Не заполнено поле \"content\"")
    @Test
    public void noContent() {
        newArticlesList.add(new Article("MadeNewArticleTitle",
                                     null,
                                      "AuthorAuthor",
                           LocalDate.of(2023,1,1)));
        List<Article> article = worker.prepareArticles(newArticlesList);
        assertEquals(0, article.size());
    }
    @DisplayName("Не заполнено поле \"author\"")
    @Test
    public void noAuthor() {
        newArticlesList.add(new Article("MadeNewArticleTitle",
                                     "ContentContent",
                                      null,
                           LocalDate.of(2023,1,1)));
        List<Article> article = worker.prepareArticles(newArticlesList);
        assertEquals(0, article.size());
    }
    @DisplayName("Установка текущей даты при ее отсутствии")
    @Test
    public void  setDateNow() {
        newArticlesList.add(new Article("MadeNewArticleTitle",
                                     "ContentContent",
                                      "AuthorAuthor",
                                 null));
        List<Article> testArcticleDate = worker.prepareArticles(newArticlesList);
        assertEquals(testArcticleDate.get(0).getCreationDate(), LocalDate.now());
    }
    @AfterEach
    public void afterEachTest (TestInfo testInfo) {System.out.println("Тест: " + testInfo.getDisplayName() + "\nРезультат: Тест пройден успешно\n");}




}