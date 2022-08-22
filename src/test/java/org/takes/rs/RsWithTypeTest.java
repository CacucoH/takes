/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2022 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.takes.rs;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import org.cactoos.text.Joined;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.IsText;

/**
 * Test case for {@link RsWithType}.
 * @since 0.16.9
 */
final class RsWithTypeTest {

    /**
     * Carriage return constant.
     */
    private static final String CRLF = "\r\n";

    /**
     * Content type text/html.
     */
    private static final String TYPE_HTML = "text/html";

    /**
     * Content type text/xml.
     */
    private static final String TYPE_XML = "text/xml";

    /**
     * Content type text/plain.
     */
    private static final String TYPE_TEXT = "text/plain";

    /**
     * Content type application/json.
     */
    private static final String TYPE_JSON = "application/json";

    /**
     * HTTP Status No Content.
     */
    private static final String HTTP_NO_CONTENT = "HTTP/1.1 204 No Content";

    /**
     * Content-Type format.
     */
    private static final String CONTENT_TYPE = "Content-Type: %s";

    /**
     * Content-Type format with charset.
     */
    private static final String TYPE_WITH_CHARSET =
        "Content-Type: %s; charset=%s";

    @Test
    void replaceTypeToResponse() {
        final String type = RsWithTypeTest.TYPE_TEXT;
        MatcherAssert.assertThat(
            new RsPrint(
                new RsWithType(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_XML),
                    type
                )
            ),
            new IsText(
                new Joined(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_NO_CONTENT,
                    String.format(RsWithTypeTest.CONTENT_TYPE, type),
                    "",
                    ""
                )
            )
        );
    }

    @Test
    void doesNotReplaceResponseCode() {
        final String body = "Error!";
        MatcherAssert.assertThat(
            new RsPrint(
                new RsWithType(
                    new RsWithBody(
                        new RsWithStatus(HttpURLConnection.HTTP_INTERNAL_ERROR),
                        body
                    ),
                    RsWithTypeTest.TYPE_HTML
                )
            ),
            new IsText(
                new Joined(
                    RsWithTypeTest.CRLF,
                    "HTTP/1.1 500 Internal Server Error",
                    "Content-Length: 6",
                    "Content-Type: text/html",
                    "",
                    body
                )
            )
        );
    }

    @Test
    void replacesTypeWithHtml() {
        MatcherAssert.assertThat(
            new RsPrint(
                new RsWithType.Html(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_XML)
                )
            ),
            new IsText(
                new Joined(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_NO_CONTENT,
                    String.format(
                        RsWithTypeTest.CONTENT_TYPE,
                        RsWithTypeTest.TYPE_HTML
                    ),
                    "",
                    ""
                )
            )
        );
        MatcherAssert.assertThat(
            new RsPrint(
                new RsWithType.Html(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_XML),
                    StandardCharsets.ISO_8859_1
                )
            ),
            new IsText(
                new Joined(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_NO_CONTENT,
                    String.format(
                        RsWithTypeTest.TYPE_WITH_CHARSET,
                        RsWithTypeTest.TYPE_HTML,
                        StandardCharsets.ISO_8859_1
                    ),
                    "",
                    ""
                )
            )
        );
    }

    @Test
    void replacesTypeWithJson() {
        MatcherAssert.assertThat(
            new RsPrint(
                new RsWithType.Json(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_XML)
                )
            ),
            new IsText(
                new Joined(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_NO_CONTENT,
                    String.format(
                        RsWithTypeTest.CONTENT_TYPE,
                        RsWithTypeTest.TYPE_JSON
                    ),
                    "",
                    ""
                )
            )
        );
        MatcherAssert.assertThat(
            new RsPrint(
                new RsWithType.Json(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_XML),
                    StandardCharsets.ISO_8859_1
                )
            ),
            new IsText(
                new Joined(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_NO_CONTENT,
                    String.format(
                        RsWithTypeTest.TYPE_WITH_CHARSET,
                        RsWithTypeTest.TYPE_JSON,
                        StandardCharsets.ISO_8859_1
                    ),
                    "",
                    ""
                )
            )
        );
    }

    @Test
    void replacesTypeWithXml() {
        MatcherAssert.assertThat(
            new RsPrint(
                new RsWithType.Xml(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_HTML)
                )
            ),
            new IsText(
                new Joined(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_NO_CONTENT,
                    String.format(
                        RsWithTypeTest.CONTENT_TYPE, RsWithTypeTest.TYPE_XML
                    ),
                    "",
                    ""
                )
            )
        );
        MatcherAssert.assertThat(
            new RsPrint(
                new RsWithType.Xml(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_HTML),
                    StandardCharsets.ISO_8859_1
                )
            ),
            new IsText(
                new Joined(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_NO_CONTENT,
                    String.format(
                        RsWithTypeTest.TYPE_WITH_CHARSET,
                        RsWithTypeTest.TYPE_XML,
                        StandardCharsets.ISO_8859_1
                    ),
                    "",
                    ""
                )
            )
        );
    }

    @Test
    void replacesTypeWithText() {
        MatcherAssert.assertThat(
            new RsPrint(
                new RsWithType.Text(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_HTML)
                )
            ),
            new IsText(
                new Joined(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_NO_CONTENT,
                    String.format(
                        RsWithTypeTest.CONTENT_TYPE, RsWithTypeTest.TYPE_TEXT
                    ),
                    "",
                    ""
                )
            )
        );
        MatcherAssert.assertThat(
            new RsPrint(
                new RsWithType.Text(
                    new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_HTML),
                    StandardCharsets.ISO_8859_1
                )
            ),
            new IsText(
                new Joined(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_NO_CONTENT,
                    String.format(
                        RsWithTypeTest.TYPE_WITH_CHARSET,
                        RsWithTypeTest.TYPE_TEXT,
                        StandardCharsets.ISO_8859_1
                    ),
                    "",
                    ""
                )
            )
        );
    }

    @Test
    void addsContentType() {
        MatcherAssert.assertThat(
            new RsPrint(
                new RsWithType(new RsEmpty(), RsWithTypeTest.TYPE_TEXT)
            ),
            new IsText(
                new Joined(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_NO_CONTENT,
                    String.format(
                        RsWithTypeTest.CONTENT_TYPE,
                        RsWithTypeTest.TYPE_TEXT
                    ),
                    "",
                    ""
                )
            )
        );
    }

    @Test
    void addsCharsetToContentType() {
        MatcherAssert.assertThat(
            new RsPrint(
                new RsWithType(
                    new RsEmpty(),
                    RsWithTypeTest.TYPE_TEXT,
                    StandardCharsets.ISO_8859_1
                )
            ),
            new IsText(
                new Joined(
                    RsWithTypeTest.CRLF,
                    RsWithTypeTest.HTTP_NO_CONTENT,
                    String.format(
                        RsWithTypeTest.TYPE_WITH_CHARSET,
                        RsWithTypeTest.TYPE_TEXT,
                        StandardCharsets.ISO_8859_1
                    ),
                    "",
                    ""
                )
            )
        );
    }
}
