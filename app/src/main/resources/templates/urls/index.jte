@import hexlet.code.util.NamedRoutes
@import java.text.SimpleDateFormat
@import hexlet.code.dto.urls.UrlsPage
@param UrlsPage page

@template.layout.page(
    page = page,
    content = @`
        <div class="container-lg mt-5">
            <h1>Сайты</h1>

            <table class="table table-bordered table-hover mt-3">
                <thead>
                <tr>
                    <th class="col-1">ID</th>
                    <th>Имя</th>
                    <th class="col-2">Последняя проверка</th>
                    <th class="col-1">Код ответа</th>
                </tr>
                </thead>
                <tbody>
                    @for(var url : page.getUrls())
                        !{var latestCheck = page.getLatestChecksByUrlId().get(url.getId());}
                        <tr>
                            <td>${url.getId()}</td>
                            <td>
                                <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                            </td>
                            @if (latestCheck != null)
                                <td>${new SimpleDateFormat("dd/MM/yyyy HH:mm").format(latestCheck.getCreatedAt())}</td>
                                <td>${latestCheck.getStatusCode()}</td>
                            @else
                                <td></td>
                                <td></td>
                            @endif
                        </tr>
                    @endfor
                </tbody>
            </table>
        </div>
    `
)
