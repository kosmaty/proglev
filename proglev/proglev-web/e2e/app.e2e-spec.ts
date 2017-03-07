import { ProglevWebPage } from './app.po';

describe('proglev-web App', () => {
  let page: ProglevWebPage;

  beforeEach(() => {
    page = new ProglevWebPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
