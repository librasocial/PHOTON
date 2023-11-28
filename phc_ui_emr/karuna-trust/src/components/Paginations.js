import React, { useState, useEffect } from "react";
import "../css/CreateEncounter.css";

const range = (from, to, step = 1) => {
  let i = from;
  const range = [];

  while (i <= to) {
    range.push(i);
    i += step;
  }

  return range;
};

const Paginations = (props) => {
  const { pageLimit, pageNeighbours, onPageChanged, currentPage } = props;
  const totalRecords = props.totalRecords;

  const [totalPages, setTotalPages] = useState(0);
  useEffect(() => {
    setTotalPages(Math.ceil(totalRecords / pageLimit));
  }, [totalRecords]);

  const fetchPageNumbers = () => {
    const totalNumbers = pageNeighbours * 0;
    const totalBlocks = totalNumbers + 1;

    if (totalPages > totalBlocks) {
      const startPage = Math.max(1, currentPage - pageNeighbours);
      const endPage = Math.min(totalPages - 1, currentPage + pageNeighbours);

      let pages = range(startPage, endPage);

      // const hasLeftSpill = startPage > 1;
      // const hasRightSpill = totalPages - endPage > 1;
      const spillOffset = totalNumbers - (pages.length + 1);

      // switch (true) {
      //     case !hasLeftSpill && !hasRightSpill: {
      //         const extraPages = range(startPage - spillOffset, startPage - 1);
      //         pages = [LEFT_PAGE, ...extraPages, ...pages];
      //         break;
      //     }
      //     case !hasLeftSpill && !hasRightSpill:
      //     default: {
      //         pages = [LEFT_PAGE, ...pages, RIGHT_PAGE];
      //         break;
      //     }
      // }
      return [...pages, totalPages];
    }
    return range(1, totalPages);
  };

  const pages = fetchPageNumbers() || [];
  return (
    <nav aria-label="Countries Pagination" className="custom-pagination">
      <ul className="pagination">
        {/* {totalPages > 2 ? */}
        <React.Fragment>
          {currentPage === 1 ? (
            <li className="page-item prev-btn disabled">
              <a
                href="/"
                className="page-link"
                aria-label="Previous"
                onClick={(e) => onPageChanged(e, currentPage - 1)}
              >
                <span aria-hidden="true">Prev</span>
              </a>
            </li>
          ) : (
            <li className="page-item prev-btn ">
              <a
                href="/"
                className="page-link"
                aria-label="Previous"
                onClick={(e) => onPageChanged(e, currentPage - 1)}
              >
                <span aria-hidden="true">Prev</span>
              </a>
            </li>
          )}
        </React.Fragment>
        {/* : ''} */}
        {pages.map((page, index) => {
          return (
            <div key={index}>
              <li
                key={index}
                className={`page-item${currentPage === page ? " active" : ""}`}
              >
                <a
                  className="page-link"
                  href="/"
                  onClick={(e) => onPageChanged(e, page)}
                >
                  {page}
                </a>
              </li>
            </div>
          );
        })}
        {/* {totalPages > 2 ? */}
        <React.Fragment>
          {currentPage === totalPages ? (
            <li className="page-item next-btn disabled">
              <a
                className="page-link"
                href="/"
                aria-label="Next"
                onClick={(e) => onPageChanged(e, currentPage + 1)}
              >
                <span aria-hidden="true">Next</span>
              </a>
            </li>
          ) : (
            <li className="page-item next-btn">
              <a
                className="page-link"
                href="/"
                aria-label="Next"
                onClick={(e) => onPageChanged(e, currentPage + 1)}
              >
                <span aria-hidden="true">Next</span>
              </a>
            </li>
          )}
        </React.Fragment>
        {/* : ''} */}
      </ul>
    </nav>
  );
};

export default Paginations;
